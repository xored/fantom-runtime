//
// Copyright (c) 2010, xored software, Inc.
// Licensed under the Academic Free License version 3.0
//
// History:
//   21 Apr 10  Andrey Talnikov  Initial Contribution
//

using javaBytecode

**
** UTF8StringTest
**
class UTF8StringTest : Test
{
  Void test()
  {
    JavaType c := JavaType.fromStream(typeof.pod.file(`/resources/UTF8String.cls`).in)
    verifyEq("UTF8String", c.type.canonicalName)
    verifyEq(2, c.methods.size)
    verifyNotNull(c.methods.find { "aæ五bc" == it.name } )
  }
}
