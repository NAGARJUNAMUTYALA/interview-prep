package com.example.interviewprep.practice.streams;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("50 Practical Stream API Exercises & Interview Scenarios")
class StreamApiPracticeTest {

    record Emp(int id, String name, String dept, double salary, List<String> skills) {}

    private List<Emp> employees;

    @BeforeEach
    void setUp() {
        employees = List.of(
                new Emp(1, "Asha", "Engineering", 95000, List.of("Java", "Spring", "Docker")),
                new Emp(2, "Vikram", "Engineering", 120000, List.of("Java", "Kubernetes", "AWS")),
                new Emp(3, "Rohan", "Finance", 80000, List.of("Excel", "Accounting")),
                new Emp(4, "Priya", "Finance", 110000, List.of("Financial Modeling", "Python")),
                new Emp(5, "Ananya", "HR", 70000, List.of("Recruiting", "Communication")),
                new Emp(6, "Karan", "Engineering", 85000, List.of("Java", "SQL")),
                new Emp(7, "Sneha", "Marketing", 75000, List.of("SEO", "Content"))
        );
    }

    // ─── Problems 1 to 10: Filtering, Sorting, Max/Min, Grouping ────────────────

    @Test
    @DisplayName("01. Filter employees belonging to Engineering")
    void test01_FilterByDepartment() {
        List<Emp> eng = employees.stream()
                .filter(e -> "Engineering".equalsIgnoreCase(e.dept()))
                .toList();
        assertEquals(3, eng.size());
    }

    @Test
    @DisplayName("02. Sort employees by salary descending")
    void test02_SortBySalaryDescending() {
        List<Emp> sorted = employees.stream()
                .sorted(Comparator.comparingDouble(Emp::salary).reversed())
                .toList();
        assertEquals("Vikram", sorted.get(0).name());
        assertEquals("Ananya", sorted.get(sorted.size() - 1).name());
    }

    @Test
    @DisplayName("03. Find highest-paid employee")
    void test03_FindHighestPaidEmployee() {
        Optional<Emp> highest = employees.stream()
                .max(Comparator.comparingDouble(Emp::salary));
        assertTrue(highest.isPresent());
        assertEquals(120000, highest.get().salary());
    }

    @Test
    @DisplayName("04. Find second-highest salary in organization")
    void test04_FindSecondHighestSalary() {
        Optional<Double> secondHighest = employees.stream()
                .map(Emp::salary)
                .distinct()
                .sorted(Comparator.reverseOrder())
                .skip(1)
                .findFirst();
        assertTrue(secondHighest.isPresent());
        assertEquals(110000, secondHighest.get());
    }

    @Test
    @DisplayName("05. Group employees by department")
    void test05_GroupByDepartment() {
        Map<String, List<Emp>> byDept = employees.stream()
                .collect(Collectors.groupingBy(Emp::dept));
        assertEquals(3, byDept.get("Engineering").size());
        assertEquals(2, byDept.get("Finance").size());
    }

    @Test
    @DisplayName("06. Calculate average salary by department")
    void test06_AverageSalaryByDepartment() {
        Map<String, Double> avgSalary = employees.stream()
                .collect(Collectors.groupingBy(Emp::dept, Collectors.averagingDouble(Emp::salary)));
        assertEquals(100000.0, avgSalary.get("Engineering"));
        assertEquals(95000.0, avgSalary.get("Finance"));
    }

    @Test
    @DisplayName("07. Partition employees by salary >= 90000")
    void test07_PartitionBySalaryThreshold() {
        Map<Boolean, List<Emp>> partitioned = employees.stream()
                .collect(Collectors.partitioningBy(e -> e.salary() >= 90000));
        assertEquals(3, partitioned.get(true).size()); // Asha (95k), Vikram (120k), Priya (110k)
        assertEquals(4, partitioned.get(false).size());
    }

    @Test
    @DisplayName("08. Flatten nested skills across all employees (flatMap)")
    void test08_FlattenSkills() {
        Set<String> distinctSkills = employees.stream()
                .flatMap(e -> e.skills().stream())
                .collect(Collectors.toSet());
        assertTrue(distinctSkills.contains("Java"));
        assertTrue(distinctSkills.contains("Docker"));
    }

    @Test
    @DisplayName("09. Count occurrences of each skill across employees")
    void test09_CountSkillFrequencies() {
        Map<String, Long> skillCounts = employees.stream()
                .flatMap(e -> e.skills().stream())
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        assertEquals(3L, skillCounts.get("Java"));
    }

    @Test
    @DisplayName("10. Find department with the highest total salary")
    void test10_DepartmentWithHighestTotalSalary() {
        Optional<Map.Entry<String, Double>> topDept = employees.stream()
                .collect(Collectors.groupingBy(Emp::dept, Collectors.summingDouble(Emp::salary)))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue());
        assertTrue(topDept.isPresent());
        assertEquals("Engineering", topDept.get().getKey());
        assertEquals(300000.0, topDept.get().getValue());
    }

    // ─── Problems 11 to 20: Reductions, Joining, Maps & Summaries ───────────────

    @Test
    @DisplayName("11. Join all employee names into a comma-separated string")
    void test11_JoinEmployeeNames() {
        String names = employees.stream()
                .map(Emp::name)
                .collect(Collectors.joining(", ", "Employees: [", "]"));
        assertTrue(names.startsWith("Employees: [Asha, Vikram"));
    }

    @Test
    @DisplayName("12. Convert employee list to Map<ID, Name>")
    void test12_ListToMap() {
        Map<Integer, String> idToName = employees.stream()
                .collect(Collectors.toMap(Emp::id, Emp::name));
        assertEquals("Asha", idToName.get(1));
    }

    @Test
    @DisplayName("13. Compute DoubleSummaryStatistics for salaries")
    void test13_SalaryStatistics() {
        DoubleSummaryStatistics stats = employees.stream()
                .collect(Collectors.summarizingDouble(Emp::salary));
        assertEquals(7, stats.getCount());
        assertEquals(70000, stats.getMin());
        assertEquals(120000, stats.getMax());
        assertEquals(635000, stats.getSum());
    }

    @Test
    @DisplayName("14. Find first employee in Finance department safely")
    void test14_FindFirstInFinance() {
        Optional<Emp> firstFinance = employees.stream()
                .filter(e -> "Finance".equals(e.dept()))
                .findFirst();
        assertTrue(firstFinance.isPresent());
        assertEquals("Rohan", firstFinance.get().name());
    }

    @Test
    @DisplayName("15. Check anyMatch, allMatch, noneMatch")
    void test15_MatchPredicates() {
        boolean anyHighEarners = employees.stream().anyMatch(e -> e.salary() > 100000);
        boolean allAdultSalaries = employees.stream().allMatch(e -> e.salary() > 50000);
        boolean noneNegative = employees.stream().noneMatch(e -> e.salary() < 0);

        assertTrue(anyHighEarners);
        assertTrue(allAdultSalaries);
        assertTrue(noneNegative);
    }

    @Test
    @DisplayName("16. Collect top 2 highest earning employees")
    void test16_Top2HighestEarners() {
        List<String> top2 = employees.stream()
                .sorted(Comparator.comparingDouble(Emp::salary).reversed())
                .limit(2)
                .map(Emp::name)
                .toList();
        assertEquals(List.of("Vikram", "Priya"), top2);
    }

    @Test
    @DisplayName("17. Group employee names by department (mapping collector)")
    void test17_GroupNamesByDepartment() {
        Map<String, List<String>> deptNames = employees.stream()
                .collect(Collectors.groupingBy(
                        Emp::dept,
                        Collectors.mapping(Emp::name, Collectors.toList())
                ));
        assertEquals(List.of("Asha", "Vikram", "Karan"), deptNames.get("Engineering"));
    }

    @Test
    @DisplayName("18. Find highest earner in each department")
    void test18_HighestEarnerPerDepartment() {
        Map<String, Optional<Emp>> topPerDept = employees.stream()
                .collect(Collectors.groupingBy(
                        Emp::dept,
                        Collectors.maxBy(Comparator.comparingDouble(Emp::salary))
                ));
        assertEquals("Vikram", topPerDept.get("Engineering").get().name());
        assertEquals("Priya", topPerDept.get("Finance").get().name());
    }

    @Test
    @DisplayName("19. Total salary payout using reduce()")
    void test19_TotalSalaryReduce() {
        double total = employees.stream()
                .map(Emp::salary)
                .reduce(0.0, Double::sum);
        assertEquals(635000.0, total);
    }

    @Test
    @DisplayName("20. Count duplicate words in a sentence")
    void test20_WordFrequencyCounter() {
        String text = "java spring boot java microservices spring java";
        Map<String, Long> counts = Arrays.stream(text.split("\\s+"))
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        assertEquals(3L, counts.get("java"));
        assertEquals(2L, counts.get("spring"));
    }

    // ─── Problems 21 to 30: Numeric Streams & Advanced Transformations ──────────

    @Test
    @DisplayName("21. IntStream sum of range [1, 100]")
    void test21_IntStreamSum() {
        int sum = IntStream.rangeClosed(1, 100).sum();
        assertEquals(5050, sum);
    }

    @Test
    @DisplayName("22. Find prime numbers using IntStream")
    void test22_PrimeNumbers() {
        List<Integer> primes = IntStream.rangeClosed(2, 20)
                .filter(n -> IntStream.rangeClosed(2, (int) Math.sqrt(n)).noneMatch(i -> n % i == 0))
                .boxed()
                .toList();
        assertEquals(List.of(2, 3, 5, 7, 11, 13, 17, 19), primes);
    }

    @Test
    @DisplayName("23. Find duplicate numbers in a list")
    void test23_FindDuplicates() {
        List<Integer> numbers = List.of(1, 2, 3, 4, 2, 5, 3, 6);
        Set<Integer> seen = new HashSet<>();
        Set<Integer> duplicates = numbers.stream()
                .filter(n -> !seen.add(n))
                .collect(Collectors.toSet());
        assertEquals(Set.of(2, 3), duplicates);
    }

    @Test
    @DisplayName("24. Reverse words in a string using Streams")
    void test24_ReverseWords() {
        String str = "Hello World Java";
        List<String> words = Arrays.asList(str.split(" "));
        Collections.reverse(words);
        String reversed = String.join(" ", words);
        assertEquals("Java World Hello", reversed);
    }

    @Test
    @DisplayName("25. Count non-empty strings")
    void test25_CountNonEmptyStrings() {
        List<String> list = List.of("abc", "", "bc", "efg", "abcd", "", "jkl");
        long count = list.stream().filter(s -> !s.isEmpty()).count();
        assertEquals(5, count);
    }

    @Test
    @DisplayName("26. Square of even numbers only")
    void test26_SquareEvenNumbers() {
        List<Integer> numbers = List.of(1, 2, 3, 4, 5, 6);
        List<Integer> squared = numbers.stream()
                .filter(n -> n % 2 == 0)
                .map(n -> n * n)
                .toList();
        assertEquals(List.of(4, 16, 36), squared);
    }

    @Test
    @DisplayName("27. Find maximum element in array with Stream")
    void test27_MaxInArray() {
        int[] arr = {12, 45, 67, 23, 89, 34};
        int max = Arrays.stream(arr).max().orElseThrow();
        assertEquals(89, max);
    }

    @Test
    @DisplayName("28. Concatenate multiple lists into single list (Stream.concat)")
    void test28_StreamConcat() {
        List<String> l1 = List.of("A", "B");
        List<String> l2 = List.of("C", "D");
        List<String> combined = Stream.concat(l1.stream(), l2.stream()).toList();
        assertEquals(List.of("A", "B", "C", "D"), combined);
    }

    @Test
    @DisplayName("29. Convert String to uppercase and join with hyphen")
    void test29_UppercaseAndJoin() {
        List<String> list = List.of("apple", "banana", "cherry");
        String result = list.stream().map(String::toUpperCase).collect(Collectors.joining("-"));
        assertEquals("APPLE-BANANA-CHERRY", result);
    }

    @Test
    @DisplayName("30. Find first non-repeated character in a string")
    void test30_FirstNonRepeatedChar() {
        String input = "swiss";
        Character result = input.chars()
                .mapToObj(c -> (char) c)
                .collect(Collectors.groupingBy(Function.identity(), LinkedHashMap::new, Collectors.counting()))
                .entrySet().stream()
                .filter(entry -> entry.getValue() == 1L)
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse(null);
        assertEquals('w', result);
    }

    // ─── Problems 31 to 40: Advanced Grouping, Partitioning & Filtering ─────────

    @Test
    @DisplayName("31. Group integers by odd and even")
    void test31_OddEvenGrouping() {
        Map<Boolean, List<Integer>> oddEven = IntStream.rangeClosed(1, 10).boxed()
                .collect(Collectors.partitioningBy(n -> n % 2 == 0));
        assertEquals(5, oddEven.get(true).size()); // even
        assertEquals(5, oddEven.get(false).size()); // odd
    }

    @Test
    @DisplayName("32. Sort map by keys")
    void test32_SortMapByKeys() {
        Map<String, Integer> unsorted = Map.of("c", 3, "a", 1, "b", 2);
        LinkedHashMap<String, Integer> sorted = unsorted.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
        assertEquals(List.of("a", "b", "c"), new ArrayList<>(sorted.keySet()));
    }

    @Test
    @DisplayName("33. Sort map by values descending")
    void test33_SortMapByValues() {
        Map<String, Integer> scores = Map.of("John", 85, "Alice", 95, "Bob", 75);
        LinkedHashMap<String, Integer> sorted = scores.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
        assertEquals("Alice", sorted.keySet().iterator().next());
    }

    @Test
    @DisplayName("34. Calculate product of all numbers (reduce)")
    void test34_ProductOfNumbers() {
        List<Integer> nums = List.of(2, 3, 4, 5);
        int product = nums.stream().reduce(1, (a, b) -> a * b);
        assertEquals(120, product);
    }

    @Test
    @DisplayName("35. Remove null values from stream")
    void test35_RemoveNulls() {
        List<String> list = Arrays.asList("A", null, "B", null, "C");
        List<String> nonNulls = list.stream().filter(Objects::nonNull).toList();
        assertEquals(List.of("A", "B", "C"), nonNulls);
    }

    @Test
    @DisplayName("36. Find average of list of doubles")
    void test36_AverageOfList() {
        List<Double> doubles = List.of(10.0, 20.0, 30.0, 40.0);
        double avg = doubles.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
        assertEquals(25.0, avg);
    }

    @Test
    @DisplayName("37. Check if list contains a specific element ignoring case")
    void test37_ContainsIgnoreCase() {
        List<String> list = List.of("Admin", "User", "Manager");
        boolean exists = list.stream().anyMatch("admin"::equalsIgnoreCase);
        assertTrue(exists);
    }

    @Test
    @DisplayName("38. Create Map of String length to list of Strings")
    void test38_GroupByLength() {
        List<String> words = List.of("to", "be", "or", "not", "to", "be");
        Map<Integer, List<String>> byLen = words.stream().collect(Collectors.groupingBy(String::length));
        assertEquals(5, byLen.get(2).size());
        assertEquals(1, byLen.get(3).size()); // "not"
    }

    @Test
    @DisplayName("39. Flatten 2D array into 1D list")
    void test39_Flatten2DArray() {
        Integer[][] matrix = {{1, 2}, {3, 4}, {5, 6}};
        List<Integer> flat = Arrays.stream(matrix).flatMap(Arrays::stream).toList();
        assertEquals(List.of(1, 2, 3, 4, 5, 6), flat);
    }

    @Test
    @DisplayName("40. Count frequency of characters in a string")
    void test40_CharacterFrequency() {
        String s = "banana";
        Map<Character, Long> freq = s.chars()
                .mapToObj(c -> (char) c)
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        assertEquals(3L, freq.get('a'));
        assertEquals(2L, freq.get('n'));
        assertEquals(1L, freq.get('b'));
    }

    // ─── Problems 41 to 50: Complex Interview Scenarios ─────────────────────────

    @Test
    @DisplayName("41. Collect unique elements preserving insertion order")
    void test41_UniquePreservingOrder() {
        List<Integer> nums = List.of(5, 2, 5, 3, 2, 8, 1);
        List<Integer> unique = nums.stream().distinct().toList();
        assertEquals(List.of(5, 2, 3, 8, 1), unique);
    }

    @Test
    @DisplayName("42. Calculate factorial using IntStream")
    void test42_FactorialIntStream() {
        int n = 5;
        long factorial = IntStream.rangeClosed(1, n).reduce(1, (a, b) -> a * b);
        assertEquals(120, factorial);
    }

    @Test
    @DisplayName("43. Generate Fibonacci sequence of first N numbers")
    void test43_FibonacciStream() {
        List<Integer> fib = Stream.iterate(new int[]{0, 1}, t -> new int[]{t[1], t[0] + t[1]})
                .limit(7)
                .map(t -> t[0])
                .toList();
        assertEquals(List.of(0, 1, 1, 2, 3, 5, 8), fib);
    }

    @Test
    @DisplayName("44. Peek at stream elements for debugging without mutating")
    void test44_PeekDebugging() {
        List<String> seen = new ArrayList<>();
        List<String> processed = List.of("one", "two", "three").stream()
                .peek(seen::add)
                .map(String::toUpperCase)
                .toList();
        assertEquals(List.of("ONE", "TWO", "THREE"), processed);
        assertEquals(3, seen.size());
    }

    @Test
    @DisplayName("45. Group by department and count employees")
    void test45_CountEmployeesPerDept() {
        Map<String, Long> counts = employees.stream()
                .collect(Collectors.groupingBy(Emp::dept, Collectors.counting()));
        assertEquals(3L, counts.get("Engineering"));
        assertEquals(2L, counts.get("Finance"));
    }

    @Test
    @DisplayName("46. Find employees with skill 'Java'")
    void test46_FilterByNestedSkill() {
        List<String> javaDevs = employees.stream()
                .filter(e -> e.skills().contains("Java"))
                .map(Emp::name)
                .toList();
        assertEquals(List.of("Asha", "Vikram", "Karan"), javaDevs);
    }

    @Test
    @DisplayName("47. Convert primitive array to boxed List")
    void test47_BoxedIntArray() {
        int[] primitives = {10, 20, 30};
        List<Integer> boxed = Arrays.stream(primitives).boxed().toList();
        assertEquals(List.of(10, 20, 30), boxed);
    }

    @Test
    @DisplayName("48. Verify Stream lazy evaluation: intermediate operations execute only on terminal operation")
    void test48_LazyEvaluationProof() {
        List<String> logs = new ArrayList<>();
        Stream<String> stream = List.of("A", "B", "C").stream()
                .filter(s -> {
                    logs.add("Filtered: " + s);
                    return true;
                });

        assertEquals(0, logs.size(), "No intermediate operation executes prior to terminal call");
        long count = stream.count();
        assertEquals(3, count);
        assertEquals(3, logs.size(), "Intermediate operations executed upon terminal call invocation");
    }

    @Test
    @DisplayName("49. Safe map to unmodifiable list")
    void test49_UnmodifiableList() {
        List<String> names = employees.stream().map(Emp::name).toList();
        assertThrows(UnsupportedOperationException.class, () -> names.add("NewEmployee"));
    }

    @Test
    @DisplayName("50. Parallel Stream for compute-intensive summing")
    void test50_ParallelStreamSum() {
        long sum = IntStream.rangeClosed(1, 1000).parallel().sum();
        assertEquals(500500, sum);
    }
}
