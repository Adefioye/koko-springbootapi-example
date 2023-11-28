import {
  Dialog,
  DialogClose,
  DialogContent,
  DialogDescription,
  DialogFooter,
  DialogHeader,
  DialogTitle,
} from "@/components/ui/dialog";
import { Button } from "./ui/button";
import { deleteCustomer } from "@/services/clients";
import { AxiosError } from "axios";
import { toast } from "./ui/use-toast";

interface Props {
  openDialog: boolean;
  setOpenDialog: React.Dispatch<React.SetStateAction<boolean>>;
  customerName: string;
  customerId: number;
  fetchCustomers: () => void;
}

const DeleteCustomerDialog = ({
  openDialog,
  setOpenDialog,
  customerName,
  customerId,
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
            Do you really want to delete {customerName}?
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
              onClick={() => handleDeleteCustomer(customerId)}
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
