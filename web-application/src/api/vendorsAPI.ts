import axios from "axios";
import { VENDORS_URL } from "../constants/urls";
import Vendor from "../model/Vendor";

export const getVendors = () => axios.get<Vendor[]>(VENDORS_URL);