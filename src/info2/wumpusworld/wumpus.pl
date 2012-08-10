% Autor:
% Fecha: 27/07/2012


%Declaracion de Variables dinamicas
:- dynamic ([
            agente_ubicacion/1,
            oro_ubicacion/1,
            dim_tablero/1,
            percepciones/1,
            sentido_agente/1,
            visitado/1
             ]).

%MAIN

iniciar :-
          ini_tablero,
          pos_inicial_agente.

%Inicializacion
ini_tablero :-
    retractall( dim_tablero(_) ),
    assert( dim_tablero(4) ),
        
	retractall( visitado(_) ),
	assert( visitado([]) ),
	
	retractall( hay_Wumpus(_,_) ),
	assert( hay_Wumpus(no,[1,1]) ),
	retractall( hay_Pozos(_,_) ),
	assert( hay_Pozos(no,[1,1]) ),
	
	retractall( oro_ubicacion(_) ),
	assert( oro_ubicacion([0,0]) ),
	
	retractall( percepciones(_) ),
	assert( percepciones([no,no,no]) ).

pos_inicial_agente :-
    retractall( agente_ubicacion(_) ),
    assert( agente_ubicacion([1,1]) ),
        
    retractall( sentido_agente(_) ),
    assert( sentido_agente(este) ),
        
    visit([1,1]).

visit(Xs) :-
    visitado(Ys),
    retractall( visitado(_) ),
    assert( visitado([Xs|Ys]) ),
	format("Visitado:~p~n", [Xs]).

%------------------------------------------------------------------------------
%Actualizar
        
actualizar_agente_ubicacion(NewAU) :-
    retractall( agente_ubicacion(_) ),
    assert( agente_ubicacion(NewAU) ),
        
    visit(NewAU).
        
%------------------------------------------------------------------------------
%KB
add_wumpus_KB(no) :-
    agente_ubicacion([X,Y]),
    Z1 is Y+1, ( permitido([X,Z1]) -> asumir_wumpus(no,[X,Z1]); true),
    Z2 is Y-1, ( permitido([X,Z2]) -> asumir_wumpus(no,[X,Z2]); true),
    Z3 is X+1, ( permitido([Z3,Y]) -> asumir_wumpus(no,[Z3,Y]); true),
    Z4 is X-1, ( permitido([Z4,Y]) -> asumir_wumpus(no,[Z4,Y]); true).

add_wumpus_KB(yes) :-
    agente_ubicacion([X,Y]),
    Z1 is Y+1, ( permitido([X,Z1]) -> ( not(hay_Wumpus(no,[X,Z1])) -> asumir_wumpus(yes,[X,Z1]);true) ;true),
    Z2 is Y-1, ( permitido([X,Z2]) -> ( not(hay_Wumpus(no,[X,Z2])) -> asumir_wumpus(yes,[X,Z2]);true) ;true),
    Z3 is X+1, ( permitido([Z3,Y]) -> ( not(hay_Wumpus(no,[Z3,Y])) -> asumir_wumpus(yes,[Z3,Y]);true) ;true),
    Z4 is X-1, ( permitido([Z4,Y]) -> ( not(hay_Wumpus(no,[Z4,Y])) -> asumir_wumpus(yes,[Z4,Y]);true) ;true).

add_pozo_KB(no) :-
    agente_ubicacion([X,Y]),        
    Z1 is Y+1, ( permitido([X,Z1]) -> asumir_pozo(no,[X,Z1]); true),
    Z2 is Y-1, ( permitido([X,Z2]) -> asumir_pozo(no,[X,Z2]); true),
    Z3 is X+1, ( permitido([Z3,Y]) -> asumir_pozo(no,[Z3,Y]); true),
    Z4 is X-1, ( permitido([Z4,Y]) -> asumir_pozo(no,[Z4,Y]); true).

add_pozo_KB(yes) :-
    agente_ubicacion([X,Y]),
    Z1 is Y+1, ( permitido([X,Z1]) -> ( not(hay_Pozos(no,[X,Z1])) -> asumir_pozo(yes,[X,Z1]);true) ;true),
    Z2 is Y-1, ( permitido([X,Z2]) -> ( not(hay_Pozos(no,[X,Z2])) -> asumir_pozo(yes,[X,Z2]);true) ;true),
    Z3 is X+1, ( permitido([Z3,Y]) -> ( not(hay_Pozos(no,[Z3,Y])) -> asumir_pozo(yes,[Z3,Y]);true) ;true),
    Z4 is X-1, ( permitido([Z4,Y]) -> ( not(hay_Pozos(no,[Z4,Y])) -> asumir_pozo(yes,[Z4,Y]);true) ;true).

add_oro_KB(no) :-
    agente_ubicacion([X,Y]),
    asumir_oro(no, [X,Y]).

add_oro_KB(yes) :-
    agente_ubicacion([X,Y]),
    asumir_oro(yes, [X,Y]).

        
asumir_wumpus(no, L) :-
    retractall( hay_Wumpus(_, L) ),
    assert( hay_Wumpus(no, L) ),
	format("No hay Wumpus en ~p~n",[L]).

asumir_wumpus(yes, L) :-
	retractall( hay_Wumpus(_, L) ),
    assert( hay_Wumpus(yes, L) ),
	format("Hay Wumpus en ~p~n",[L]).

asumir_pozo(no, L) :-
    retractall( hay_Pozos(_, L) ),
    assert( hay_Pozos(no, L) ),
	format("No hay Pozo en ~p~n",[L]).
    
asumir_pozo(yes, L) :-
    retractall( hay_Pozos(_, L) ),
    assert( hay_Pozos(yes, L) ),
	format("Hay Pozo en ~p~n",[L]).
    
asumir_oro(no, L) :-
    retractall( hay_Oro(_, L) ),
    assert( hay_Oro(no, L) ).

asumir_oro(yes, L) :-
    retractall( oro_ubicacion(_) ),
    assert( oro_ubicacion(L) ),
	format("Hay Oro en ~p~n",[L]).

%Knowledge Base:
ask_KB(Accion) :-
        
		percepciones([Olor,Briza,Brillo]),
        
        add_wumpus_KB(Olor),
        add_pozo_KB(Briza),
        add_oro_KB(Brillo),
        
        agente_ubicacion(AL),
        oro_ubicacion(GL),
        
		%disparar,
                
		visitado(VisitedList),

		( not(AL=GL) ->
		( ( hay_Wumpus(no,L), hay_Pozos(no,L), not_member(L, VisitedList) ) -> 
		movimientos(L,Accion); movimientos([1,1],Accion))
		;format("WON!~n", []), movimientos([1,1], VL), Accion=[[agarrar]|VL]).

permitido([X,Y]) :-
    dim_tablero(WS),
    0 < X, X < WS+1,
    0 < Y, Y < WS+1.

%------------------------------------------------------------------------------
%UTILS
not_member(X, []).
not_member([X,Y], [[U,V]|Ys]) :-
    ( X=U,Y=V -> fail
    ; not_member([X,Y], Ys)
    ).

        
movimientos([X,Y], Accion) :-

        sentido_agente(S),
        
		elegir_mov(S,[X,Y], Accion).
		
elegir_mov(este,[X,Y],Accion):- mov_este([X,Y],Accion).
elegir_mov(oeste,[X,Y],Accion):- mov_oeste([X,Y],Accion).
elegir_mov(norte,[X,Y],Accion):- mov_norte([X,Y],Accion).
elegir_mov(sur,[X,Y],Accion):- mov_sur([X,Y],Accion).
	
mov_este([X,Y], Accion):-
	
	agente_ubicacion([Xs,Ys]),
	
	( not([X,Y]=[Xs,Ys]) ->
	
	Z1 is Xs+1, ( [X,Y]=[Z1,Ys] -> Accion=[avanzar]; true),
    Z2 is Xs-1, ( [X,Y]=[Z2,Ys] -> Accion=[girarIzq,girarIzq,avanzar], retractall( sentido_agente(_) ), assert(sentido_agente(oeste)), actualizar_agente_ubicacion(([X,Y])) ; true), 
	Z3 is Ys+1, ( [X,Y]=[Xs,Z3] -> Accion=[girarIzq,avanzar], retractall( sentido_agente(_) ), assert(sentido_agente(norte)), actualizar_agente_ubicacion(([X,Y])) ; true), 
	Z4 is Ys-1, ( [X,Y]=[Xs,Z4] -> Accion=[girarDer,avanzar], retractall( sentido_agente(_) ), assert(sentido_agente(sur)), actualizar_agente_ubicacion(([X,Y])) ; true),
	
	( [X,Y]=[Z1,Z3] -> Accion=[avanzar,girarIzq,avanzar],  retractall( sentido_agente(_) ), assert(sentido_agente(norte)),actualizar_agente_ubicacion(([X,Y])); true),
    ( [X,Y]=[Z1,Z4] -> Accion=[girarDer,avanzar,girarIzq,avanzar], retractall( sentido_agente(_) ), assert(sentido_agente(este)), actualizar_agente_ubicacion(([X,Y])) ; true), 
	( [X,Y]=[Z2,Z3] -> Accion=[girarIzq,avanzar,girarIzq,avanzar], retractall( sentido_agente(_) ), assert(sentido_agente(oeste)), actualizar_agente_ubicacion(([X,Y])) ; true), 
	( [X,Y]=[Z2,Z4] -> Accion=[girarDer,avanzar,girarDer,avanzar], retractall( sentido_agente(_) ), assert(sentido_agente(oeste)), actualizar_agente_ubicacion(([X,Y])) ; true)
	
	; Accion=[]).
	
mov_oeste([X,Y], Accion):-
	
	agente_ubicacion([Xs,Ys]),

	( not([X,Y]=[Xs,Ys]) ->
	
	Z5 is Xs+1, ( [X,Y]=[Z5,Ys] -> Accion=[girarIzq,girarIzq,avanzar], retractall( sentido_agente(_) ), assert(sentido_agente(este)), actualizar_agente_ubicacion(([X,Y])) ; true),
	Z6 is Xs-1, ( [X,Y]=[Z6,Ys] -> Accion=[avanzar], actualizar_agente_ubicacion(([X,Y])); true), 
	Z6 is Ys+1, ( [X,Y]=[Xs,Z7] -> Accion=[girarDer,avanzar], retractall( sentido_agente(_) ), assert(sentido_agente(norte)), actualizar_agente_ubicacion(([X,Y])) ; true), 
	Z8 is Ys-1, ( [X,Y]=[Xs,Z8] -> Accion=[girarIzq,avanzar], retractall( sentido_agente(_) ), assert(sentido_agente(sur)), actualizar_agente_ubicacion(([X,Y])) ; true),
	  
	( [X,Y]=[Z5,Z7] -> Accion=[girarDer,avanzar,girarDer,avanzar],  retractall( sentido_agente(_) ), assert(sentido_agente(este)),actualizar_agente_ubicacion(([X,Y])); true),
    ( [X,Y]=[Z5,Z8] -> Accion=[girarIzq,avanzar,girarIzq,avanzar], retractall( sentido_agente(_) ), assert(sentido_agente(este)), actualizar_agente_ubicacion(([X,Y])) ; true), 
	( [X,Y]=[Z6,Z7] -> Accion=[girarDer,avanzar,girarIzq,avanzar], retractall( sentido_agente(_) ), assert(sentido_agente(oeste)), actualizar_agente_ubicacion(([X,Y])) ; true), 
	( [X,Y]=[Z6,Z8] -> Accion=[girarIzq,avanzar,girarDer,avanzar], retractall( sentido_agente(_) ), assert(sentido_agente(oeste)), actualizar_agente_ubicacion(([X,Y])) ; true) 
	
	; Accion=[]).	
		
mov_norte([X,Y], Accion):-

	agente_ubicacion([Xs,Ys]),
	
	( not([X,Y]=[Xs,Ys]) ->
	
	Z9 is Xs+1, ( [X,Y]=[Z9,Ys] -> Accion=[girarDer,avanzar], retractall( sentido_agente(_) ), assert(sentido_agente(este)), actualizar_agente_ubicacion(([X,Y])) ; true), 
	Z10 is Xs-1, ( [X,Y]=[Z10,Ys] -> Accion=[girarIzq,avanzar], retractall( sentido_agente(_) ), assert(sentido_agente(oeste)), actualizar_agente_ubicacion(([X,Y])) ; true), 
	Z11 is Ys+1, ( [X,Y]=[Xs,Z11] -> Accion=[avanzar], actualizar_agente_ubicacion(([X,Y])); true), 
	Z12 is Ys-1, ( [X,Y]=[Xs,Z12] -> Accion=[girarDer,girarDer,avanzar], retractall( sentido_agente(_) ), assert(sentido_agente(sur)), actualizar_agente_ubicacion(([X,Y])) ; true),
	
	( [X,Y]=[Z9,Z11] -> Accion=[avanzar,girarDer,avanzar],  retractall( sentido_agente(_) ), assert(sentido_agente(este)),actualizar_agente_ubicacion(([X,Y])); true),
    ( [X,Y]=[Z9,Z12] -> Accion=[girarDer,girarDer,avanzar,girarIzq,avanzar], retractall( sentido_agente(_) ), assert(sentido_agente(este)), actualizar_agente_ubicacion(([X,Y])) ; true), 
	( [X,Y]=[Z10,Z11] -> Accion=[avanzar,girarIzq,avanzar], retractall( sentido_agente(_) ), assert(sentido_agente(oeste)), actualizar_agente_ubicacion(([X,Y])) ; true), 
	( [X,Y]=[Z10,Z12] -> Accion=[girarDer,girarDer,avanzar,girarDer,avanzar], retractall( sentido_agente(_) ), assert(sentido_agente(oeste)), actualizar_agente_ubicacion(([X,Y])) ; true)
	
	; Accion=[]).
	
mov_sur([X,Y],Accion):-

	agente_ubicacion([Xs,Ys]),

	( not([X,Y]=[Xs,Ys]) ->
	
	
	Z1 is Xs+1, ( [X,Y]=[Z1,Ys] -> Accion=[girarIzq,avanzar],  retractall( sentido_agente(_) ), assert(sentido_agente(este)), actualizar_agente_ubicacion(([X,Y])); true),
	Z2 is Xs-1, ( [X,Y]=[Z2,Ys] -> Accion=[girarDer,avanzar], retractall( sentido_agente(_) ), assert(sentido_agente(oeste)), actualizar_agente_ubicacion(([X,Y])) ; true),
	Z3 is Ys+1, ( [X,Y]=[Xs,Z3] -> Accion=[girarIzq,girarIzq,avanzar], retractall( sentido_agente(_) ), assert(sentido_agente(norte)), actualizar_agente_ubicacion(([X,Y])) ; true),
	Z4 is Ys-1, ( [X,Y]=[Xs,Z4] -> Accion=[avanzar], actualizar_agente_ubicacion(([X,Y])); true),
  
    ( [X,Y]=[Z1,Z3] -> Accion=[girarDer,girarDer,avanzar,girarDer,avanzar],  retractall( sentido_agente(_) ), assert(sentido_agente(este)),actualizar_agente_ubicacion(([X,Y])); true),
    ( [X,Y]=[Z1,Z4] -> Accion=[avanzar,girarIzq,avanzar], retractall( sentido_agente(_) ), assert(sentido_agente(este)), actualizar_agente_ubicacion(([X,Y])) ; true), 
	( [X,Y]=[Z2,Z3] -> Accion=[girarDer,girarDer,avanzar,girarIzq,avanzar], retractall( sentido_agente(_) ), assert(sentido_agente(oeste)), actualizar_agente_ubicacion(([X,Y])) ; true), 
	( [X,Y]=[Z2,Z4] -> Accion=[avanzar,girarDer,avanzar], retractall( sentido_agente(_) ), assert(sentido_agente(oeste)), actualizar_agente_ubicacion(([X,Y])) ; true)
	
	; Accion=[]).
	
enviar_percepciones(Olor,Briza,Brillo):-
		
		retractall(percepciones(_)),
		assert(percepciones([Olor,Briza,Brillo])).
		  
%------------------------------------------------------------------------------