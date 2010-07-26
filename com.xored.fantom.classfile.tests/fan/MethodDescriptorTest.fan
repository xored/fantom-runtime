//
// Copyright (c) 2010, xored software, Inc.
// Licensed under the Academic Free License version 3.0
//
// History:
//   6 May 10  Andrey Talnikov  Initial Contribution
//

using javaBytecode

**
** MethodDescriptorTest
**
class MethodDescriptorTest : Test
{
  Void test()
  {
    c := JavaType.fromStream(typeof.pod.file(`/resources/MethodDescriptor.cls`).in)

    method1 := c.methods.find{ it.name == "test1" }
    verifyEq(TypeRef.Boolean, method1.returns)
    method1Params := method1.params
    verifyEq(method1Params.size, 2)
    verifyEq(TypeRef.Int, method1Params[0])
    verifyEq(TypeRef.arrayOf(TypeRef.Int), method1Params[1])
    
    method2 := c.methods.find{ it.name == "test2" }
    verifyEq(TypeRef.fromCanonicalName("java.lang.Object"), method2.returns)
    method2Params := method2.params
    verifyEq(method2Params.size, 2)
    verifyEq(TypeRef.fromCanonicalName("java.lang.String"), method2Params[0])
    verifyEq(TypeRef.arrayOf(TypeRef.fromCanonicalName("java.lang.String")), method2Params[1])
  }
}
