import { zodResolver } from "@hookform/resolvers/zod";
import { AxiosError } from "axios";
import { useForm } from "react-hook-form";
import { z } from "zod";
import { Button } from "../ui/button";
import {
  FormField,
  FormItem,
  FormLabel,
  FormControl,
  FormMessage,
  Form,
} from "../ui/form";
import { Input } from "../ui/input";
import { toast } from "../ui/use-toast";
import { useNavigate } from "react-router-dom";
import { genderProps } from "../../../types";
import { saveCustomer } from "@/services/clients";
import {
  Select,
  SelectTrigger,
  SelectValue,
  SelectContent,
  SelectItem,
} from "../ui/select";
import { useAuth } from "@/context/AuthContext";
import { useEffect } from "react";

const formSchema = z.object({
  name: z.string().min(5).max(30),
  email: z.string().email(),
  password: z
    .string()
    .regex(new RegExp(".*[A-Z].*"), "Require 1 uppercase character")
    .regex(new RegExp(".*[a-z].*"), "Require 1 lowercase character")
    .regex(new RegExp(".*\\d.*"), "Require 1 number")
    .regex(
      new RegExp(".*[`~<>?,./!@#$%^&*()\\-_+=\"'|{}\\[\\];:\\\\].*"),
      "Require 1 special character"
    )
    .min(8, "Require at least 8 characters"),
  age: z.coerce.number().min(16).max(100),
  gender: z.nativeEnum(genderProps),
});

const SignupForm = () => {
  const navigate = useNavigate();
  const { login: performLogin } = useAuth();
  const form = useForm<z.infer<typeof formSchema>>({
    mode: "onBlur",
    resolver: zodResolver(formSchema),
    defaultValues: {
      name: "",
      email: "",
      password: "",
      gender: genderProps.MALE,
      age: 5,
    },
  });

  async function onSubmit(values: z.infer<typeof formSchema>) {
    try {
      const userNameAndPassword = {
        username: values.email,
        password: values.password,
      };
      await saveCustomer(values);
      await performLogin(userNameAndPassword);
      navigate("/dashboard");
      console.log("Successfully signed up and logged in.");
    } catch (error) {
      if (error instanceof AxiosError) {
        toast({
          variant: "destructive",
          title: `${error.code}`,
          description: `${error.response?.data?.message}`,
        });
      }
    }
  }

  useEffect(() => {
    form.reset();
  }, [form, form.formState.isSubmitted]);

  return (
    <div className="min-h-screen flex flex-col items-center justify-center text-white bg-slate-600">
      <div className="w-3/5 shadow-2xl rounded-2xl bg-slate-500 p-10 md:w-2/5">
        <p className="font-semibold text-lg mb-3 text-center">
          Sign up to your account
        </p>
        <Form {...form}>
          <form className="w-full" onSubmit={form.handleSubmit(onSubmit)}>
            <div className="space-y-3">
              <FormField
                control={form.control}
                name="name"
                render={({ field }) => (
                  <FormItem className="">
                    <FormLabel htmlFor="name" className="text-right">
                      Name
                    </FormLabel>
                    <FormControl>
                      <Input
                        type="text"
                        id="name"
                        className="bg-inherit focus-visible:ring-transparent"
                        {...field}
                      />
                    </FormControl>
                    <FormMessage className="text-left" />
                  </FormItem>
                )}
              />
              <FormField
                control={form.control}
                name="email"
                render={({ field }) => (
                  <FormItem className="">
                    <FormLabel htmlFor="email" className="text-right">
                      Email
                    </FormLabel>
                    <FormControl>
                      <Input
                        type="email"
                        id="email"
                        className="bg-inherit focus-visible:ring-0"
                        {...field}
                      />
                    </FormControl>
                    <FormMessage className="text-left" />
                  </FormItem>
                )}
              />
              <FormField
                control={form.control}
                name="password"
                render={({ field }) => (
                  <FormItem className="">
                    <FormLabel htmlFor="password" className="text-right">
                      Password
                    </FormLabel>
                    <FormControl>
                      <Input
                        type="password"
                        id="password"
                        className="bg-inherit focus-visible:ring-0"
                        {...field}
                      />
                    </FormControl>
                    <FormMessage className="text-left" />
                  </FormItem>
                )}
              />
              <FormField
                control={form.control}
                name="gender"
                render={({ field }) => (
                  <FormItem className="">
                    <FormLabel htmlFor="gender" className="text-right">
                      Gender
                    </FormLabel>
                    <FormControl>
                      <Select {...field} onValueChange={field.onChange}>
                        <SelectTrigger className="bg-inherit focus-visible:ring-0">
                          <SelectValue placeholder="Select a gender" />
                        </SelectTrigger>
                        <SelectContent>
                          <SelectItem value="MALE">MALE</SelectItem>
                          <SelectItem value="FEMALE">FEMALE</SelectItem>
                        </SelectContent>
                      </Select>
                    </FormControl>
                    <FormMessage className="text-left" />
                  </FormItem>
                )}
              />
              <FormField
                control={form.control}
                name="age"
                render={({ field }) => (
                  <FormItem className="">
                    <FormLabel htmlFor="age" className="text-right">
                      Age
                    </FormLabel>
                    <FormControl>
                      <Input
                        type="number"
                        id="age"
                        className="bg-inherit focus-visible:ring-0"
                        {...field}
                      />
                    </FormControl>
                    <FormMessage className="text-left" />
                  </FormItem>
                )}
              />
              <Button
                type="submit"
                disabled={
                  !form.formState.isValid || form.formState.isSubmitting
                }
                onClick={() => {}}
                className="w-full"
              >
                Sign up
              </Button>
            </div>
          </form>
        </Form>
      </div>
    </div>
  );
};

export default SignupForm;
