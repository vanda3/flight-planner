% definir precedencia do operador ":"

:- use_module(library(lists)).
:- op( 50, xfy, :).
:- op(60, xfy, /).

timetable(edinburgh,london,
[ 9:40/10:50/ba4733/alldays,
13:40/14:50/ba4773/alldays,
19:40/20:50/ba4833/[mo,tu,we,th,fr,su]]).

timetable(london,edinburgh,
[ 9:40/10:50/ba4732/alldays,
11:40/12:50/ba4752/alldays,
18:40/19:50/ba4822/[mo,tu,we,th,fr]]).

timetable(london,ljubljana,
[13:20/16:20/ju201/[fr],
13:20/16:20/ju213/[su]]).

timetable(london,zurich,
[ 9:10/11:45/ba614/alldays,
14:45/17:20/sr805/alldays]).

timetable(london,milan,
[ 8:30/11:20/ba510/alldays,
11:00/13:50/az459/alldays]).

timetable(ljubljana,zurich,
[11:30/12:40/ju322/[tu,th]]).

timetable(ljubljana,london,
[11:10/12:20/yu200/[fr],
11:25/12:20/yu212/[su]]).

timetable(milan,london,
[ 9:10/10:00/az458/alldays,
12:20/13:10/ba511/alldays]).

timetable(milan,zurich,
[ 9:25/10:15/sr621/alldays,
12:45/13:35/sr623/alldays]).

timetable(zurich,ljubljana,
[13:30/14:40/yu323/[tu,th]]).

timetable(zurich,london,
[ 9:00/9:40/ba613/[mo,tu,we,th,fr,sa],
16:10/16:55/sr806/[mo,tu,we,th,fr,su]]).

timetable(zurich,milan,
[ 7:55/8:45/sr620/alldays]).


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Define o que e um voo

flight(Place1,Place2,Day,Flightnum,Deptime,Arrtime):-
	timetable(Place1, Place2, Flightlist),
	member(Deptime/Arrtime/Flightnum/Daylist,Flightlist),
	existsday(Day,Daylist).
    
% Indica se o Day e um predicado valido

existsday(Day, alldays):-
	member(Day,[mo,tu,we,th,fr,sa,su]).
existsday(Day,Daylist):-
	member(Day,Daylist).
    
deptime([Place1/Place2/Flightnum/Dep|_],Dep).



%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Pergunta 1
% Encontra os dias da semana em que ha voo direto de Place1 para Place2

directflight(Place1,Place2):-
	findall(Day, flight(Place1,Place2,Day,_,_,_),List),   % encontra todos os dias em que existe o voo e adiciona a List
	sort(List,X),   % remover elementos repetidos
	write(X),
	nl.


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Pergunta 2
% Viajar de Place1 para Place2 no dia Day

% voo direto
route(Place1, Place2, Day, [Place1/Place2/Flightnum/Deptime])  :-
	flight(Place1,Place2,Day,Flightnum,Deptime,_).


% voo indireto
route(Place1, Place2, Day, [(Place1/Place3/Flightnum/Deptime)|Route])  :-
    flight(Place1, Place3, Day, Flightnum, Deptime, Arrtime),
	route(Place3, Place2, Day, Route),        % procura voo direto
	deptime(Route, Deptime2),    % departure time de Route e Deptime2
	transfer(Arrtime, Deptime2).      % verifica se existe tempo suficiente para fazer a transferencia

% Indica se e possivel fazer a transferencia (tempo >= 40 min)

transfer( H1:M1, H2:M2)  :-
	60 * (H2 - H1) + M2 - M1 >= 40.


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% sequence(london,[zurich,ljubljana,milan],tu,fr).
% Pergunta 3
% Viajar da cidade S para 3 cidades entre os dias DepDay e ArrDay - ida e volta, 1 voo por dia

sequence(S, CitiesList, DepDay, ArrDay) :-
    findall(P, permutation(CitiesList, P), [C|Tail]), % [C|Tail] contem todas as permutacoes de CitiesList
    aux(DepDay, ArrDay, S, C, Tail).

aux(DepDay, ArrDay, S, Cities, [Route|RestRoutes]) :-
    indexOf(Cities, C1, 0),
    indexOf(Cities, C2, 1),
    indexOf(Cities, C3, 2),
    getnext(DepDay, NextDay),
    getnext(NextDay, NextNextDay),  
    (((flight(S,C1,DepDay,_,_,_),!,
    flight(C1,C2,NextDay,_,_,_),!,
    flight(C2,C3,NextNextDay,_,_,_),!,
    flight(C3,S,ArrDay,_,_,_),!)->(write('C1: '),write(C1),nl, write('C2: '),write(C2),nl, write('C3: '),write(C3),nl));(aux(DepDay, ArrDay, S, Route, RestRoutes))).

getnext(D1, D2) :-
	indexOf([mo,tu,we,th,fr,sa,su], D1, I1),
    NewI1 is I1+1,
	match([mo,tu,we,th,fr,sa,su], NewI1, D2), !.
getCity(Name, CityList, Index) :-
	indexOf(CityList, Name, Index).
match([H|_],0,H) :-
    !.
match([_|T],N,H) :-
    N > 0,
    N1 is N-1,
    match(T,N1,H).
indexOf([Element|_], Element, 0). % We found the element
indexOf([_|Tail], Element, Index):-
  indexOf(Tail, Element, Index1), % Check in the tail of the list
  Index is Index1+1. 