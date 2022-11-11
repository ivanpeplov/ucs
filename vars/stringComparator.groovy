def strings = ["banana", "fig", "apple", "orange", "pie"];
def comparator = new Comparator<String>() {
    @Override
    int compare(String o1, String o2) {
        return o1.length() != o2.length() ? o1.length() <=> o2.length() : o1 <=> o2;
    }
}
Collections.sort(strings, comparator);
for (def s : strings) {
    println s
}
//Result: [fig, pie, apple, banana, orange]