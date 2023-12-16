import {
  Dialog,
  DialogClose,
  DialogContent,
  DialogDescription,
  DialogFooter,
  DialogHeader,
  DialogTitle,
} from "@/components/ui/dialog";
import { Button } from "../ui/button";
import { deleteCustomer } from "@/services/clients";
import { AxiosError } from "axios";
import { toast } from "../ui/use-toast";
import { Customer } from "./../../../types";

interface Props {
  openDialog: boolean;
  setOpenDialog: React.Dispatch<React.SetStateAction<boolean>>;
  customer: Customer;
  fetchCustomers: () => void;
}

const DeleteCustomerDialog = ({
  openDialog,
  setOpenDialog,
  customer,
  fetchCustomers,
}: Props) => {
  async function handleDeleteCustomer(id: number) {
    try {
      await deleteCustomer(id);
      toast({
        title: `Deleted customer`,
        description: "Customer succesfully deleted",
      });
      fetchCustomers();
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
    <Dialog open={openDialog} onOpenChange={setOpenDialog}>
      <DialogContent>
        <DialogHeader>
          <DialogTitle>
            Do you really want to delete {customer.name!}?
          </DialogTitle>
          <DialogDescription>
            This action cannot be undone. This will permanently delete customer
            from our servers.
          </DialogDescription>
        </DialogHeader>
        <DialogFooter className="sm:justify-start md:justify-end">
          <DialogClose asChild>
            <Button
              type="button"
              variant="destructive"
              onClick={() => handleDeleteCustomer(customer.id!)}
            >
              Delete
            </Button>
          </DialogClose>
        </DialogFooter>
      </DialogContent>
    </Dialog>
  );
};

export default DeleteCustomerDialog;
