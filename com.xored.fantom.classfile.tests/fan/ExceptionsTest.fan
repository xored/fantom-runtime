//
// Copyright (c) 2010, xored software, Inc.
// Licensed under the Academic Free License version 3.0
//
// History:
//   6 May 10  Andrey Talnikov  Initial Contribution
//

using javaBytecode

**
** ExceptionsTest
**
class ExceptionsTest : Test
{
  Void test()
  {
    c := JavaType.fromStream(typeof.pod.file(`/resources/Exceptions.cls`).in)
    exceptions := c.methods.find{ it.name == "test" }.exceptions
    verifyEq(exceptions.size, 2)
    verifyNotNull(exceptions.find{ it.canonicalName == "java.io.IOException" })
    verifyNotNull(exceptions.find{ it.canonicalName == "java.lang.InterruptedException" })
  }
}
