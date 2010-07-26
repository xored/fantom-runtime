//
// Copyright (c) 2010, xored software, Inc.
// Licensed under the Academic Free License version 3.0
//
// History:
//   21 Apr 10  Andrey Talnikov  Initial Contribution
//

using javaBytecode

**
** EnumTest
**
class EnumTest : Test
{
  Void test()
  {
    JavaType c := JavaType.fromStream(typeof.pod.file(`/resources/SimpleEnum.cls`).in)
  }
}
