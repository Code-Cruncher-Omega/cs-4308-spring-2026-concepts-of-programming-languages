with Ada.Text_IO; use Ada.Text_IO;
with Complex_Numbers; use Complex_Numbers;

procedure Main is

   Complex_Number1 : Complex_Numbers;
   Complex_Number2 : Complex_Numbers;

   Input : Integer;

begin
   while True loop
      Put_Line ("=== Complex Number Calculator ===");
      Put_Line ("First complex number is: " & Float'Image (Complex_Number1.Get_Real) & " + (" & Float'Image (Complex_Number1.Get_Complex) & ")i");
      Put_Line ("Second complex number is: " & Float'Image (Complex_Number2.Get_Real) & " + (" & Float'Image (Complex_Number2.Get_Complex) & ")i");
      Put_Line ("");
      Put_Line ("1. Change first complex number");
      Put_Line ("2. Change second complex number");
      Put_Line ("3. Print sum");
      Put_Line ("4. Print difference");
      Put_Line ("5. Print product");
      Put_Line ("6. Print quotient");
      Put_Line ("7. Quit program");
      Put ("Enter a number to choose an action: ");

      Input := Integer'Value (Get_Line);

      if Input = 1 then
         Put ("Enter the new real value: ");
         Complex_Number1.Set_Real (Integer'Value (Get_Line));

         Put ("Enter the new complex value: ");
         Complex_Number1.Set_Complex (Integer'Value (Get_Line));
      elsif Input = 2 then
         Put ("Enter the new real value: ");
         Complex_Number2.Set_Real (Integer'Value (Get_Line));

         Put ("Enter the new complex value: ");
         Complex_Number2.Set_Complex (Integer'Value (Get_Line));
      elsif Input = 3 then
         Result : Complex_Number := Complex_Number1.Calculate_Sum (Complex_Number2);
         Put_Line ("Sum: " & Integer'Float (Result.Get_Real) & " + (" & Float'Image (Result.Get_Complex) & ")i");
      elsif Input = 4 then

      elsif Input = 5 then

      elsif Input = 6 then

      elsif Input = 7 then
         exit;
      else
         Put_Line("Error: Invalid input!");
      end if;


   end loop;

   Put_Line("Quitting program...");

   null;
end Main;
