//
// Copyright (c) 2010, xored software, Inc.
// Licensed under the Academic Free License version 3.0
//
// History:
//   6 May 10  Andrey Talnikov  Initial Contribution
//

using javaBytecode

**
** FieldsTest
**
class FieldsTest : Test
{
  Void verifyFieldType(JavaType c, Str name, TypeRef type)
  {
    verifyEq(type, c.fields.find{name == it.name}.type)
  }

  Void test()
  {
    c := JavaType.fromStream(typeof.pod.file(`/resources/Fields.cls`).in)
    verifyEq(10, c.fields.size)
    verifyFieldType(c, "a", TypeRef.Byte)
    verifyFieldType(c, "b", TypeRef.Char)
    verifyFieldType(c, "c", TypeRef.Double)
    verifyFieldType(c, "d", TypeRef.Float)
    verifyFieldType(c, "e", TypeRef.Int)
    verifyFieldType(c, "f", TypeRef.Long)
    verifyFieldType(c, "g", TypeRef.Short)
    verifyFieldType(c, "h", TypeRef.Boolean)
    verifyFieldType(c, "o", TypeRef.fromCanonicalName("java.lang.Object"))
    verifyFieldType(c, "s", TypeRef.fromCanonicalName("java.lang.String"))
  }
}
