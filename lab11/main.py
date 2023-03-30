from graphviz import Digraph

# funkcja zwraca relacje zależności
def relation_D(A, I):
    return [(i,j) for i in A for j in A if (i,j) not in I ]

# funkcja zwraca ślad [w] wzgęldem podanej relacje niezależności
'''Początkowo dla każdych akcji znajdujących się obok siebie w podanym słowie sprawdza możliwość przestawienia ich. 
Jeżeli akcje są niezależne i jest taka mozliowść przestawia je i za pomocą rekursji odpala funkcje dla nowo powstałego słowa.'''
def trace(word, I, traces):
    if word in traces:
        return
    traces.append(word)
    for i in range(len(word) - 1):
        if (word[i], word[i+1]) in I:
            letter_list = list(word)
            letter_list[i], letter_list[i+1] = letter_list[i+1],letter_list[i]
            trace(''.join(letter_list), I, traces)

# Poniższe 3 funkcję odpowiadają za zwrócenie postaci normlanej Foaty
'''Funkcja create_stacks tworzy stosy zgodnie z algorytmem z zaproponownej pracy. Funkcja stacks_remove usuwa dla każdej akcji kolejne watości z jej stosu (jeżeli wartościa tą jest * to jest ona usuwana)
W funkcji foata form najpierw tworzony jest stos za pomocą create_stacks. Następnie póki wszystkie stosy nie będą puste pobiera wartości ze stosów za pomocą funkcji stack_remove, 
które dodawne są do listy result_letters. Ze zwróconych wartości znajdujących się w lisćie tworzone są kolejne części postaci normalnej Foaty'''
def create_stacks(A, I, w):
    dependent_relations = relation_D(A,I)
    stacks = {char: [] for char in A}
    for l in w:
        stacks[l].append(l)
        for rel in dependent_relations:
            if rel[0] == l and rel[1] != l:
                stacks[rel[1]].append('*')

    return stacks

def stack_remove(stacks):
    letters = [stacks.get(letter).pop() for letter in stacks if len(stacks.get(letter)) > 0]
    result = set(letters)
    if "*" in result:
        result.remove("*")
    return sorted(list(result))

def foata_form(A, I, w):
    stacks = create_stacks(A, I, w)
    max_size = max([len(value) for value in stacks.values()])
    result_letters = []

    for i in range(max_size):
        letters = stack_remove(stacks)
        result_letters. append(letters)

    foata = ''
    for inx in range(len(result_letters) - 1, -1, -1):
        if len(result_letters[inx]) > 0:
            foata += '(' + ''.join([i[0] for i in result_letters[inx]]) + ')'
    return foata

# Poniższe dwie funkcję odpowiadają za utwrzoenie grafu zależności dla śladu [w]
'''Na poczatku funkcją create_graph dodaje do grafu wierzchołki (każdy wierzchołek to akcja w śladzie [w]). Następnie do listy krawędzi dodawane są wszytskie możliwe krewędzie 
(tzn. jeżeli akcja x poprzadzająca akcje z jest od niej zależna krawędź (x,y) jest dodawana do listy). Następnie za pomocą funkcji remove_unecessery usuwane są nadmiarowe krawędzie.
Funkcja remove_unecessery jest funkcją rekurencyjną. Jako dane wejściowe dostaje poczatkową krawędź z której szukamy drogi do kolejnych osiagalnych wierzchołków. Jeżeli osiągniemy wierzchołek
połaczony krawędzią z wierzchołkiem od ktróego zaczynalismy krawędź ta jest usuwana (jest nadmiarowa, bo do wierzchołka można dotrzeć nieużywając jej). 
Po usunieciu wszystkich niepotrzebnych krawędzi twrzony jest graf. Do utworzenia grafau wykorzystana jest zaproponowana bilblitoeka graphviz.'''
def remove_unecessery(node, edges, copy_edges, length, inx=0):
    if (node[0], copy_edges[inx][1]) in edges and length > 0:
        edges.remove((node[0], copy_edges[inx][1]))

    for inx2 in range(inx+1, len(copy_edges)):
        if copy_edges[inx2][0] == copy_edges[inx][1]:
            length += 1
            remove_unecessery(node, edges, copy_edges, length, inx2)

def create_graph(word,I,A,id):
    graph = Digraph()
    nodes = list(word)
    nodes_t = []

    for n in range(len(nodes)):
        graph.node(str(n)+nodes[n],nodes[n])
        nodes_t.append(str(n)+nodes[n])

    D = relation_D(A,I)
    edges_t = []

    for n1 in range (len(nodes_t)):
        for n2 in range(n1+1,len(nodes_t)):
            if (nodes_t[n1][1],nodes_t[n2][1]) in D:
                edges_t.append((nodes_t[n1],nodes_t[n2]))

    for i in edges_t:
        remove_unecessery(i,edges_t, edges_t.copy(), 0, edges_t.index(i))
    for e1,e2 in edges_t:
        graph.edge(e1,e2)

    graph.render('graph'+str(id)+'.gv', view=True)
    return nodes_t,edges_t

# Poniższa funkcja odpowieda za wyznaczenie postaci normalnej Foaty z utworzonego wcześniej grafu
'''Funkcja idzie po kolejnych akcjach śladu [w], jeżeli w liscie tymczasowej nie ma akcji która posiada z obecnie sprawdzana akcją krawędzi w grafie to dodaje ten wierzchołek do listy.
W momencie gdy akcja ma krawedz z którąś z akcji w liście, tymczasowa lista jest zerowana a jej zawartość dodawana jest do listy pośredniej. 
Lista pośrednia jest przekształcana w listą końcowa usuwając niepotrzebne znaki i puste listy.
Lista końcowa zawiera listy, które są częsciami postaci normlanej. Na koniec z listy tej tworzzny jest zwracany string'''
def foata_from_graph(edges, w):
    list = []
    temp = []
    for inx in range (len(w)-1):
        temp.append(str(inx)+w[inx])
        for val in temp:
            if (val,str(inx+1)+w[inx+1]) in edges:
                list.append(temp)
                temp = []

    if len(temp) == 0:
        list.append([w[len(w)-1]])
    else:
        temp.append(w[len(w)-1])
        list.append(temp)

    result = []
    for val in list:
        if len(val) == 0:
            continue
        temp = []
        for i in val:
            if len(i) == 2:
                temp.append(i[1])
            else:
                temp.append(i)
        result.append(temp)

    foata = ''
    for val in result:
        foata += '(' + ''.join([i[0] for i in val]) + ')'
    return foata






if __name__ == '__main__':
    print("Example 1")
    A = ['a', 'b', 'c', 'd']
    I = [('a', 'd'), ('d', 'a'), ('b', 'c'), ('c', 'b')]
    w = "baadcb"
    print("Relacja zależności D: ")
    print(relation_D(A,I))
    print("Ślad [w] względem relacji I:")
    traces = []
    trace(w,I,traces)
    print(traces)
    print("Postać normalna Foaty śladu [w]:")
    print(foata_form(A,I,w))
    print("Postać normalna Foaty śladu [w] na podstawie grafu:")
    nodes, edges = create_graph(w, I, A, 1)
    print(foata_from_graph(edges, w))


    print("Example 2")
    A = ['a', 'b', 'c', 'd', 'e', 'f']
    I = [("a", "d"), ("d", "a"), ("b", "e"), ("e", "b"), ("c", "d"), ("d", "c"), ("c", "f"), ("f", "c")]
    w = "acdcfbbe"
    print("Relacja zależności D: ")
    print(relation_D(A, I))
    print("Ślad [w] względem relacji I:")
    traces = []
    trace(w, I, traces)
    print(traces)
    print("Postać normalna Foaty śladu [w]:")
    print(foata_form(A, I, w))
    print("Postać normalna Foaty śladu [w] na podstawie grafu:")
    nodes, edges = create_graph(w, I, A, 1)
    print(foata_from_graph(edges, w))

