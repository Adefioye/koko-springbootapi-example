export interface CustomerProps {
  id?: number;
  name: string;
  email: string;
  age: number;
  gender: string;
}

export const enum formActionType {
  CREATE_CUSTOMER = "CREATE_CUSTOMER",
  UPDATE_CUSTOMER = "UPDATE_CUSTOMER",
}

export enum genderProps {
  MALE = "MALE",
  FEMALE = "FEMALE",
}
