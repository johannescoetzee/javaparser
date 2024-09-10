import foo.Test;

class CallTest {
  void callTest() {
    Test t = new Test();
    Test.Foo f = new Test.Foo();
    Test.Bar r = new Test.Bar();
    Test.Baz z = new Test.Baz();

    // System.out.println(Test.test(f, 1, 2));
    // System.out.println(t.test(r, 1, 2));

    System.out.println(t.test(z, 1, 2));
  }
}
