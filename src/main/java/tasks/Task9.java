package tasks;

import common.Person;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/*
Далее вы увидите код, который специально написан максимально плохо.
Постарайтесь без ругани привести его в надлежащий вид
P.S. Код в целом рабочий (не везде), комментарии оставлены чтобы вам проще понять чего же хотел автор
P.P.S Здесь ваши правки необходимо прокомментировать (можно в коде, можно в PR на Github)
 */
public class Task9 {

  private long count;

  // Комментарий: пропускаем в stream первый элемент, у всех остальных получаем имя и собираем в список
  // Изначальный код плох тем, что изменяет исходный список
  public List<String> getNames(List<Person> persons) {
    return persons.stream()
            .skip(1)
            .map(Person::firstName).collect(Collectors.toList());
  }

  // Комментарий: здесь лучше использовать конструктор set'а, чем запускать stream,
  // так как промежуточных преобразований не делается
  public Set<String> getDifferentNames(List<Person> persons) {
    return new HashSet<>(getNames(persons));
  }

  // Комментарий: создаем stream из трех элементов, оставляем ненулевые, соединяем
  public String convertPersonToString(Person person) {
    return Stream.of(person.firstName(), person.middleName(), person.secondName())
            .filter(Objects::nonNull)
            .collect(Collectors.joining(" "));
  }

  // словарь id персоны -> ее имя
  // Комментарий: собираем из коллекции Map, используя convertPersonToString из этого же класса
  // Исправила: добавила mergeFunction
  public Map<Integer, String> getPersonNames(Collection<Person> persons) {
    return persons.stream()
            .collect(Collectors.toMap(Person::id, this::convertPersonToString, (prev, cur) -> prev));
  }

  // Скорость не изменилась: в худшем случае O(N * M), где N - длина первой коллекиции, M - второй
  // (например, обе коллекции List)
  // Исправила: добавила преобразование второй коллекции в Set
  // Тогда скорость O(M) + O(N) = O(N + M)
  public boolean hasSamePersons(Collection<Person> persons1, Collection<Person> persons2) {
    Set<Person> setOfPersons2 = new HashSet<>(persons2);
    return persons1.stream().anyMatch(setOfPersons2::contains);
  }

  // Комментарий: оставляем только четные числа, считаем количество
  // Изначально использовалась глобальная переменная.
  // Кроме того, исправленный код более понятен, так как count() предназначен как раз для таких случаев.
  public long countEven(Stream<Integer> numbers) {
    return numbers.filter(num -> num % 2 == 0).count();
  }

  // Комментарий 2: Поняла, где ошиблась. У целых чисел хешкод равен самому значению числа.
  // Когда числа кладутся в HashSet (HashMap), они кладутся по хешкоду % количество элементов.
  // Так как в Set, в который кладется n элементов сразу, выделяется памяти заведомо с запасом,
  // хешкод (= значение числа) % размер будет класть элементы в ячейки массива в порядке возрастания.
  // Для примера "1, 2, 16, 33" это не работает, потому что последние элементы будут больше начальной capacity
  // (по умолчанию она равна 16) и HashSet распределит элементы по-другому.
  // Для примера, например, "1, 2, 3, 15" порядок вывода на экран сохраняется по возрастанию.
    void listVsSet() {
    List<Integer> integers = IntStream.rangeClosed(1, 10000).boxed().collect(Collectors.toList());
    List<Integer> snapshot = new ArrayList<>(integers);
    Collections.shuffle(integers);
    Set<Integer> set = new HashSet<>(integers);
    assert snapshot.toString().equals(set.toString());
  }
}
