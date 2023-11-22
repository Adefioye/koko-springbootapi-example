import { Avatar, AvatarFallback, AvatarImage } from "@/components/ui/avatar";
import { CustomerProps } from "types";

interface Props {
  customer: CustomerProps;
}

const Card = ({ customer }: Props) => {
  return (
    <div className="flex flex-col max-w-sm items-center p-2 border border-white rounded-xl">
      <img
        src="https://images.unsplash.com/photo-1612865547334-09cb8cb455da?ixid=MXwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHw%3D&ixlib=rb-1.2.1&auto=format&fit=crop&w=634&q=80"
        alt="card pics"
        className="h-[5rem] w-[15rem] object-fit"
      />
      <Avatar className="-mt-4 border-4 border-white">
        <AvatarImage src="https://github.com/shadcn.png" />
        <AvatarFallback>CN</AvatarFallback>
      </Avatar>
      <div className="flex w-[15rem] flex-col mt-2 space-y-3 items-center break-all">
        <p>{customer.id}</p>
        <p>{customer.name}</p>
        <p>{customer.email}</p>
        <p>{customer.age}</p>
      </div>
    </div>
  );
};

export default Card;
