import { Button } from "./ui/button";
import {
  Sheet,
  SheetContent,
  SheetHeader,
  SheetTitle,
  SheetDescription,
} from "./ui/sheet";
import { Input } from "./ui/input";

import { zodResolver } from "@hookform/resolvers/zod";
import { useForm } from "react-hook-form";
import * as z from "zod";

import {
  FormField,
  FormItem,
  FormMessage,
  Form,
  FormLabel,
  FormControl,
} from "./ui/form";

import {
  Select,
  SelectTrigger,
  SelectValue,
  SelectItem,
  SelectContent,
} from "./ui/select";
import { saveCustomer } from "@/services/clients";

interface Props {
  open: boolean;
  setOpen: React.Dispatch<React.SetStateAction<boolean>>;
}

const genderType = ["MALE", "FEMALE"] as const;

const formSchema = z.object({
  name: z.string().min(5).max(30),
  email: z.string().email(),
  age: z.coerce.number().min(16).max(100),
  gender: z.enum(genderType),
});

const CustomerForm = ({ open, setOpen }: Props) => {
  const form = useForm<z.infer<typeof formSchema>>({
    resolver: zodResolver(formSchema),
    defaultValues: {
      name: "",
      email: "",
      age: 5,
    },
  });

  async function onSubmit(values: z.infer<typeof formSchema>) {
    // Do something with the form values.
    try {
      const res = await saveCustomer(values);
      return res.data;
    } catch (error) {
      console.log(error);
      throw error;
    }
  }

  return (
    <Sheet open={open} onOpenChange={setOpen}>
      <SheetContent>
        <SheetHeader>
          <SheetTitle>Edit profile</SheetTitle>
          <SheetDescription>
            Make changes to your profile here. Click save when you're done.
          </SheetDescription>
        </SheetHeader>
        <Form {...form}>
          <form
            className="grid gap-4 py-4"
            onSubmit={form.handleSubmit(onSubmit)}
          >
            <FormField
              control={form.control}
              name="name"
              render={({ field }) => (
                <FormItem className="grid grid-cols-4 items-center gap-4">
                  <FormLabel htmlFor="name" className="text-right">
                    Name
                  </FormLabel>
                  <FormControl>
                    <Input id="name" className="col-span-3" {...field} />
                  </FormControl>
                  <FormMessage className="col-span-4 !-mt-3 text-center" />
                </FormItem>
              )}
            />
            <FormField
              control={form.control}
              name="email"
              render={({ field }) => (
                <FormItem className="grid grid-cols-4 items-center gap-4">
                  <FormLabel htmlFor="email" className="text-right">
                    Email
                  </FormLabel>
                  <FormControl>
                    <Input id="email" className="col-span-3" {...field} />
                  </FormControl>
                  <FormMessage className="col-span-4 !-mt-3 text-center" />
                </FormItem>
              )}
            />
            <FormField
              control={form.control}
              name="age"
              render={({ field }) => (
                <FormItem className="grid grid-cols-4 items-center gap-4">
                  <FormLabel htmlFor="age" className="text-right">
                    Age
                  </FormLabel>
                  <FormControl>
                    <Input id="age" className="col-span-3" {...field} />
                  </FormControl>
                  <FormMessage className="col-span-4 !-mt-3 text-center" />
                </FormItem>
              )}
            />
            <FormField
              control={form.control}
              name="gender"
              render={({ field }) => (
                <FormItem className="grid grid-cols-4 items-center gap-4">
                  <FormLabel htmlFor="gender" className="text-right">
                    Gender
                  </FormLabel>
                  <FormControl id="gender">
                    <Select {...field} onValueChange={field.onChange}>
                      <SelectTrigger className="col-span-3">
                        <SelectValue placeholder="Select a gender" />
                      </SelectTrigger>
                      <SelectContent>
                        <SelectItem value="MALE">MALE</SelectItem>
                        <SelectItem value="FEMALE">FEMALE</SelectItem>
                      </SelectContent>
                    </Select>
                  </FormControl>
                  <FormMessage className="col-span-4 !-mt-3 text-center" />
                </FormItem>
              )}
            />
            <Button
              type="submit"
              disabled={!form.formState.isValid || form.formState.isSubmitting}
            >
              Save customer
            </Button>
          </form>
        </Form>
      </SheetContent>
    </Sheet>
  );
};

export default CustomerForm;
