package complex_numbers is
   
   -- Set up a "class" called 'Values' with attributes that default to 0.0
   -- 'Values' are also referred to as 'Complex[number]' in functions
   type Values is tagged record
      Real, Complex : Float := 0.0;
   end record;
   
   -- Set up functions and procedures provided by complex_numbers as well as
   -- specify their return values and parameters
   
   function Create_Complex return Values;
   
   function Calculate_Sum (Complex1, Complex2 : Values) return Values;
   
   function Calculate_Difference (Complex1, Complex2 : Values) return Values;
   
   function Calculate_Product (Complex1, Complex2 : Values) return Values;
   
   function Calculate_Quotient (Complex1, Complex2 : Values) return Values;
   
   procedure Put_Custom_Float (Number : Float);

end complex_numbers;
