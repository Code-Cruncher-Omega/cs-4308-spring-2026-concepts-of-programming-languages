-- Importing package(s)

with Ada.Text_IO;

-- Implement functions and parameters mentioned in 'complex_numbers.ads'
package body complex_numbers is
   
   -- Create and return a new instance of 'Values'
   function Create_Complex return Values is
      New_Complex : Values;
   begin
      return New_Complex;
   end Create_Complex;
   
   -- Create and return a new instance of 'Values' set to the sum of two 'Values'
   -- Results = Complex1 + Complex2
   function Calculate_Sum (Complex1, Complex2 : Values) return Values is
      Result : Values;
   begin
      Result.Real := Complex1.Real + Complex2.Real;
      Result.Complex := Complex1.Complex + Complex2.Complex;
      
      return Result;
   end Calculate_Sum;
   
   -- Create and return a new instance of 'Values' set to the difference of two 'Values'
   -- Results = Complex1 - Complex2
   function Calculate_Difference (Complex1, Complex2 : Values) return Values is
      Result : Values;
   begin
      Result.Real := Complex1.Real - Complex2.Real;
      Result.Complex := Complex1.Complex - Complex2.Complex;
      
      return Result;
   end Calculate_Difference;
   
   -- Create and return a new instance of 'Values' set to the product of two 'Values'
   -- Results = Complex1 * Complex2
   function Calculate_Product (Complex1, Complex2 : Values) return Values is 
      Result : Values; 
   begin
      Result.Real := Complex1.Real * Complex2.Real - Complex1.Complex * Complex2.Complex;
      Result.Complex := Complex1.Real * Complex2.Complex + Complex1.Complex * Complex2.Real;
      
      return Result;
   end Calculate_Product;
   
   -- Create and return a new instance of 'Values' set to the quotient of two 'Values'
   -- Results = Complex1 / Complex2
   function Calculate_Quotient (Complex1, Complex2 : Values) return Values is
      Result : Values;
      Denominator : Float := Complex2.Real ** 2 + Complex2.Complex ** 2;
   begin
      Result.Real := (Complex1.Real * Complex2.Real + Complex1.Complex * Complex2.Complex) / Denominator;
      Result.Complex := (Complex1.Complex * Complex2.Real - Complex1.Real * Complex2.Complex) / Denominator;
      
      return Result;
   end Calculate_Quotient;
   
   -- Function that prints out Float types in non-scientific notation to the
   -- hundredths place in similar fashion to Put([string])
   procedure Put_Custom_Float (Number : Float) is
      -- Creates a new child package based on Ada's provided package for handling
      -- inputs and outputs, specifically for Float types
      package Custom_Float_IO is new Ada.Text_IO.Float_IO (Float);
      use Custom_Float_IO;
      
   begin
      -- Modifies the variable that sets the upper limit as to how big the exponent
      -- can be, so no exponent will be shown when printing a Float type
      Custom_Float_IO.Default_Exp := 0;
      
      -- Changes the variable stating the maximum number of digits that will appear
      -- after the decimal, so only 2 digits will be shown after the decimal
      Custom_Float_IO.Default_Aft := 2;
      
      -- Prints out the Float type like how it is done with Put([String])
      Custom_Float_IO.Put(Item => Number);
   end Put_Custom_Float;

end complex_numbers;
