//
// Copyright (c) 2010, xored software, Inc.
// Licensed under the Academic Free License version 3.0
//
// History:
//   21 Apr 10  Andrey Talnikov  Initial Contribution
//

using javaBytecode

**
** VarargsTest
**
class VarargsTest : Test
{
  Void test()
  {
    JavaType c := JavaType.fromStream(typeof.pod.file(`/resources/Varargs.cls`).in)
    verifyEq(
      c.methods.find{ "test" == it.name }.descr.params[-1],
      TypeRef.arrayOf(TypeRef.fromCanonicalName("java.lang.String")))
  }
}
