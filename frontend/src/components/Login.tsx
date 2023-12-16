import { useForm } from "react-hook-form";
import { Button } from "./ui/button";
import {
  FormField,
  FormItem,
  FormLabel,
  FormControl,
  FormMessage,
  Form,
} from "./ui/form";
import { Input } from "./ui/input";
import { zodResolver } from "@hookform/resolvers/zod";
import { z } from "zod";
import { useAuth } from "@/context/AuthContext";
import { AxiosError } from "axios";
import { toast } from "./ui/use-toast";
import { useNavigate } from "react-router-dom";

const formSchema = z.object({
  username: z.string().min(5).max(30),
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
});

const LoginForm = () => {
  const { login } = useAuth();
  const navigate = useNavigate();
  const form = useForm<z.infer<typeof formSchema>>({
    mode: "onBlur",
    resolver: zodResolver(formSchema),
    defaultValues: {
      username: "",
      password: "",
    },
  });

  async function onSubmit(values: z.infer<typeof formSchema>) {
    try {
      await login(values);
      navigate("/dashboard");
      console.log("Successfully logged in");
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

  return (
    <div className="min-h-screen flex flex-col items-center justify-center text-white bg-slate-600">
      <div className="w-3/5 shadow-2xl rounded-2xl bg-slate-500 p-10 md:w-2/5">
        <p className="font-semibold text-lg mb-3 text-center">
          Sign in to your account
        </p>
        <Form {...form}>
          <form className="w-full" onSubmit={form.handleSubmit(onSubmit)}>
            <div className="space-y-3">
              <FormField
                control={form.control}
                name="username"
                render={({ field }) => (
                  <FormItem className="">
                    <FormLabel htmlFor="name" className="text-right">
                      Username
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
                name="password"
                render={({ field }) => (
                  <FormItem className="">
                    <FormLabel htmlFor="email" className="text-right">
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
              <Button
                type="submit"
                disabled={
                  !(form.formState.isValid && form.formState.isDirty) ||
                  form.formState.isSubmitting
                }
                onClick={() => {}}
                className="w-full"
              >
                Sign in
              </Button>
            </div>
          </form>
        </Form>
      </div>
    </div>
  );
};

export default LoginForm;
