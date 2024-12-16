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
  public Map<Integer, String> getPersonNames(Collection<Person> persons) {
    return persons.stream()
            .collect(Collectors.toMap(Person::id, this::convertPersonToString));
  }

  // Комментарий: для каждого из элементов первой коллекции вызывается contains() на второй коллекции
  // до первого совпадения (или до конца, если совпадений не было)
  public boolean hasSamePersons(Collection<Person> persons1, Collection<Person> persons2) {
    return persons1.stream().anyMatch(persons2::contains);
  }

  // Комментарий: оставляем только четные числа, считаем количество
  public long countEven(Stream<Integer> numbers) {
    return numbers.filter(num -> num % 2 == 0).count();
  }

  // Комментарий: HashSet упорядочивает элементы внутри себя, но этот порядок зависит от хешкодов элементов.
  // Это связано с тем, что HashSet является "надстройкой" над HashMap, которая представляет собой массив Entry,
  // Порядок хранения которых зависит от хешкода.
  // Для достаточно простых объектов (например, целых чисел, как в примере), порядок объектов, упорядоченных по хешкодам, будет совпадать
  // с так называемым естественным порядком объектов
    void listVsSet() {
    List<Integer> integers = IntStream.rangeClosed(1, 10000).boxed().collect(Collectors.toList());
    List<Integer> snapshot = new ArrayList<>(integers);
    Collections.shuffle(integers);
    Set<Integer> set = new HashSet<>(integers);
    assert snapshot.toString().equals(set.toString());
  }
}
