function [ x ] = simplex( c,A,b )

% initialization
m=size(A,1);
n=size(A,2);



% step 1

M=[A,eye(m)];

newc=[zeros(1,n),ones(1,m)];
 

[~,A,b,ctmp]=algorithmRun(newc,M,b);


if isempty(ctmp)
    x=[];
elseif ctmp(size(ctmp,2))>=0
    A=A(1:end,1:n);
    c
    A
    b
    [~,~,~,x]=algorithmRun(c,A,b);
else
    x=[];
end    




end

