function [ c,A,b,x ] = algorithmRun( c,A,b )
%prepare for simplex
m=size(A,1);
n=size(A,2);
I=eye(m);
baze=zeros(1,m);
c=[c,0];
for i=1:m
    for j=1:n
        if A(i,j)>-0.00001 && A(i,j)<0.00001    %because of some precision problems
            A(i,j)=0;            
        end    
        if(A(:,j) == I(:,i))
            baze(i) = j;
        end
    end
end


cmb=[A,b];

for i = 1:m
    j = baze(i);
    c = c - c(j)*cmb(i,:);
end


%run the simplex method
val=-1; %just the initialization to get in
iter=1;
boolean=1;
while val<0
    if iter>100000
        boolean=0;
        break
    end    
    
    [val,y]=min(c(1:(size(c,2)-1)));
    if cmb(:,y)<0 
        if(c(y)<0)
            boolean=0;
            break
        end    
    else
        
        tempColumn=ones(1,size(cmb,1));
        for i=1:size(cmb,1)
            if cmb(i,y)<0
                tempColumn(i)=0;
            else
                tempColumn(i)=cmb(i,y);
            end    
        end    
        
        
        [~, position]=min((cmb(:,size(cmb,2)))./tempColumn'); 
        
        
        
        pivotPosition=[position,y];
        
        
        c = c - (c(y)/cmb(pivotPosition(1),pivotPosition(2)))*cmb(pivotPosition(1),:);
        tempMatrix=ones(size(cmb));
        for i=1:m
            if i==pivotPosition(1)
                tempMatrix(i,:)=cmb(i,:)/cmb(i,y);
            else
                tempMatrix(i,:)=cmb(i,:)-(cmb(i,y)/cmb(pivotPosition(1),pivotPosition(2)))*cmb(pivotPosition(1),:);
            end    
        end    
    end
    cmb=tempMatrix;
    iter=iter+1;    
end


%form the results for the output
if boolean==0
    x=[];
    c=[];
    A=[];
    b=[];
else
    %c=c(1:(size(c,2)-1));    
    A=cmb(1:end,1:(size(cmb,2)-1));
    b=cmb(1:end,size(cmb,2));
    x=zeros(1,n);
    tmp=0;
    seq=0;
    for i=1:n
        for j=1:m
            if A(j,i)==0
                tmp=tmp+1;
            elseif A(j,i)==1
                boolean=0;
                seq=j;
            end        
        end
        if tmp==m-1 && boolean==0
            x(i)=b(seq);            
        end    
        tmp=0;
        boolean=1;
    end    
end    
end

