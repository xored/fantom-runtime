//
// Copyright (c) 2010, xored software, Inc.
// Licensed under the Academic Free License version 3.0
//
// History:
//   30 Apr 10  Andrey Talnikov  Initial Contribution
//

using javaBytecode

**
** InterfacesTest
**
class InterfacesTest : Test
{
  Void test()
  {
    c := JavaType.fromStream(typeof.pod.file(`/resources/Interfaces.cls`).in)
    verifyEq(2, c.interfaces.size)
    verifyNotNull(c.interfaces.find{ "java.io.Serializable" == it.toStr })
    verifyNotNull(c.interfaces.find{ "java.util.Collection" == it.toStr })
  }
}
