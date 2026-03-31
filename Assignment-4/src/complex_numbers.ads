package complex_numbers is
   
   type Values is tagged record
      Real, Complex : Float := 0.0;
   end record;
   
   function Create_Complex return Values;
   
   function Calculate_Sum (Complex1, Complex2 : Values) return Values;
   
   function Calculate_Difference (Complex1, Complex2 : Values) return Values;
   
   function Calculate_Product (Complex1, Complex2 : Values) return Values;
   
   function Calculate_Quotient (Complex1, Complex2 : Values) return Values;
   
   procedure Put_Custom_Float (Number : Float);

end complex_numbers;
