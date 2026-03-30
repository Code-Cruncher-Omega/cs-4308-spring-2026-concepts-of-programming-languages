package Complex_Numbers is
   
   function Get_Real return Float;
   
   function Get_Complex return Float;
   
   function Calculate_Sum (Other_Complex : Complex_Number) return Complex_Number;
   
   function Calculate_Difference (Other_Complex : Complex_Number) return Complex_Number;
   
   function Calculate_Product (Other_Complex : Complex_Number) return Complex_Number;
   
   function Calculate_Quotient (Other_Complex : Complex_Number) return Complex_Number;
   
   procedure Set_Real (New_Real : Float);
   
   procedure Set_Complex (New_Complex : Float);
   
private
   
   type Complex_Number is tagged record
      Real, Complex : Float := 0.0;
   end record;

end Complex_Numbers;


package body Complex_Numbers is
   
   function Get_Real is begin
      return Real;
   end Get_Real;
   
   function Get_Complex is begin
      return Complex;
   end Get_Complex;
   
   function Calculate_Sum (Other_Complex : Complex_Number) is begin
      Result : Complex_Number;
      Result.Set_Real (Real + Other_Complex.Get_Real);
      Result.Set_Complex (Complex + Other_Complex.Get_Complex);
      
      return Result;
   end Calculate_Sum;
   
   function Calculate_Difference (Other_Complex : Complex_Number) is begin
      Result : Complex_Number;
      Result.Set_Real (Real - Other_Complex.Get_Real);
      Result.Set_Complex (Complex - Other_Complex.Get_Complex);
      
      return Result;
   end Calculate_Difference;
   
   function Calculate_Product (Other_Complex : Complex_Number) is begin
      Result : Complex_Number;
      Result.Set_Real (Real * Other_Complex.Get_Real - Complex * Other_Complex.Get_Complex);
      Result.Set_Complex (Real * Other_Complex.Get_Complex + Complex * Other_Complex.Get_Real);
      
      return Result;
   end Calculate_Product;
   
   function Calculate_Quotient (Other_Complex : Complex_Number) is begin
      Result : Complex_Number;
      Denominator : Float := Other_Complex.Get_Real ** 2 + Other_Complex.Get_Complex ** 2;
      Result.Set_Real ((Real * Other_Complex.Get_Real + Complex * Other_Complex.Get_Complex) / Denominator);
      Result.Set_Complex ((Complex * Other_Complex.Get_Real - Real * Other_Complex.Get_Complex) / Denominator);
      
      return Result;
   end Calculate_Quotient;
   
   procedure Set_Real (New_Real : Float) is begin
      Real := New_Real;
   end Change_Real;
   
   procedure Set_Complex (New_Complex : Float) is begin
      Complex := New_Complex;
   end Change_Complex;
   
end Complex_Numbers;
