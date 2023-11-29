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
import { AxiosError } from "axios";

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
import {
  saveCustomer,
  updateCustomer as updateCustomerDetails,
} from "@/services/clients";
import { useEffect } from "react";
import { toast } from "@/components/ui/use-toast";
import { CustomerProps, genderProps } from "../../types";

interface Props {
  open: boolean;
  setOpen: React.Dispatch<React.SetStateAction<boolean>>;
  fetchCustomers: () => void;
  updateCustomer: CustomerProps | undefined;
}

const formSchema = z.object({
  name: z.string().min(5).max(30),
  email: z.string().email(),
  age: z.coerce.number().min(16).max(100),
  gender: z.nativeEnum(genderProps),
});

const CustomerForm = ({
  open,
  setOpen,
  fetchCustomers,
  updateCustomer,
}: Props) => {
  console.log("Update customer: ", updateCustomer);

  const initialFormValues = {
    name: updateCustomer?.name ?? "",
    email: updateCustomer?.email ?? "",
    age: updateCustomer?.age ?? 5,
    gender: (updateCustomer?.gender as genderProps) ?? genderProps.MALE,
  };

  const form = useForm<z.infer<typeof formSchema>>({
    mode: "onBlur",
    resolver: zodResolver(formSchema),
    defaultValues: initialFormValues,
    values: initialFormValues,
  });

  async function onSubmit(values: z.infer<typeof formSchema>) {
    // Do something with the form values.
    try {
      if (updateCustomer === undefined) {
        await saveCustomer(values);
        // To make page updated with new customer data
        fetchCustomers();
      } else {
        await updateCustomerDetails(updateCustomer.id!, values);
        // To make page updated with new customer data
        fetchCustomers();
      }

      toast({
        title: `Success: Customer ${
          updateCustomer !== undefined ? "updated" : "saved"
        }`,
        description: `Customer succesfully ${
          updateCustomer !== undefined ? "updated" : "saved"
        } in the database`,
      });
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
    <Sheet open={open} onOpenChange={setOpen}>
      <SheetContent>
        <SheetHeader>
          <SheetTitle>
            {updateCustomer === undefined ? "Add" : "Edit"} customer
          </SheetTitle>
          <SheetDescription>
            {updateCustomer === undefined ? "Add" : "Edit"} a customer here and
            save it to the database.
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
              disabled={
                !(form.formState.isValid && form.formState.isDirty) ||
                form.formState.isSubmitting
              }
              onClick={() => setOpen(false)}
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
