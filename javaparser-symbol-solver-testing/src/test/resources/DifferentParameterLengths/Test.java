package foo;

class Test {
  static class Foo { }

  static class Bar extends Foo { }
   
  static class Baz extends Bar { }

  String test(Foo s, int... xs) { return s.toString(); }
  Integer test(Bar s, int x, int y) { return x; }
}
