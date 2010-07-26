//
// Copyright (c) 2010, xored software, Inc.
// Licensed under the Academic Free License version 3.0
//
// History:
//   6 May 10  Andrey Talnikov  Initial Contribution
//

using javaBytecode

**
** FlagsTest
**
class FlagsTest : Test
{
  Void test()
  {
    c := JavaType.fromStream(typeof.pod.file(`/resources/Flags.cls`).in)
    verify(Flags.Public.isSet(c.fields.find{ "a" == it.name }.flags))
    verify(Flags.Protected.isSet(c.fields.find{ "b" == it.name }.flags))
    verify(Flags.Private.isSet(c.fields.find{ "c" == it.name }.flags))
    verify(Flags.Static.isSet(c.fields.find{ "d" == it.name }.flags))
    verify(Flags.Final.isSet(c.fields.find{ "e" == it.name }.flags))
    verify(Flags.Volatile.isSet(c.fields.find{ "f" == it.name }.flags))
    verify(Flags.Transient.isSet(c.fields.find{ "g" == it.name }.flags))
  }
}
