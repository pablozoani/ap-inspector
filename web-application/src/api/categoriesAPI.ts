import axios from "axios";
import { CATEGORIES_URL } from "../constants/urls";
import Category from "../model/Category";

export const getCategories = () => axios.get<Category[]>(CATEGORIES_URL);