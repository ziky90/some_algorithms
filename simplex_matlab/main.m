clear all;
close all;

%1)
c=[1,-1,2];
A=[-3,1,1;1,-1,+1];
b=[4;3];
x=simplex(c,A,b);
x

%2)
c=[-1,0,-3,1];
A=[1,2,3,0;2,1,5,0;1,2,1,1];
b=[15;20;10];
x=simplex(c,A,b);
x

%3)
A=[-1.27,-1.02,0,0,0,1,1,0,0;0,-1.02,-4.7,-3.09,0,1,0,1,0;0,0,0,-3.09,-9,1,0,0,1;1,1,1,1,1,0,0,0,0];
f=[0,0,0,0,0,-1,0,0,0];
b=[0;0;0;3000];
x=simplex(f,A,b);
x

%4)
struct = load('data1.mat');
x=struct.x;
y=struct.y;
m=struct.m;
n=struct.n;
v=ones(m,1);
A=[x,v,-v;-x,-v,-v];
f=[0,0,1];
b=[y;-y];
x=simplex(f,A,b);
x

