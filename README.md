# :rabbit2: Generator ewolucyjny :rabbit2:
 
Program symuluje życie zwierząt na mapie.
W menu startowym można ustawić:
- szerokość i wysokość mapy
- energię startową zwierzaka
- energię potrzebną na wykonanie ruchu
- energię za zjedzenie trawy
- stosunek jungli do stepu
- liczbę zwierzaków startowych.

Krótki opis:
- są do dyspozycji dwie mapy - z "zawijanymi" bokami oraz ograniczona
- mapy działają niezależnie, można w każdym momencie zatrzymać/wznowić symulację na każdej z nich
- każdy zwierzak ma zapisane w swoim genotypie prawdopodobieństwo ruchów jakie będzie wykonywał
- w ciągu jednego dnia każde zwierzę wykonuje jeden ruch tracąc przy tym energię
- gdy zwierzak napotka roślinę zjada ją, jeśli jest kilka zwierzaków przy jednej roślinie zjada osobnik z największą ilością posiadanej energii
- na wykresach pojawiają się w czasie rzeczywistym statyskiki związane z symulacjami 
- zwierzęta rozmnażają sie, gdy spotkają się na jednym polu (ich dziecko dziedziczy genotyp w odpowiednim stosunku energii rodziców)
- w środkowej częsci mapy znajduje się step, gdzie rośliny wystpępują z większym prawopodobieństwem 
- gdy energia zwierząt skończy się - umierają i znikają z mapy

# Demo
![DEMO](https://github.com/maati01/Generator-ewolucyjny/blob/main/demo.gif)
