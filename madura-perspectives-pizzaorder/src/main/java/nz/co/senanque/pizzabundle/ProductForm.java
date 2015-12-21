/*******************************************************************************
 * Copyright (c)2014 Prometheus Consulting
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package nz.co.senanque.pizzabundle;
import nz.co.senanque.vaadin.MaduraForm;
import nz.co.senanque.vaadin.MaduraSessionManager;

import com.vaadin.ui.VerticalLayout;

/**
 * This is a generic form that can be used for all products.
 * It generates fields for whatever product it is focused on and binds to that product
 * Rules that apply to the the product are automatically enforced so we don't need to
 * code them here.
 * 
 * @author Roger Parkinson
 * 
 */
public class ProductForm extends MaduraForm {
    private static final long serialVersionUID = 1L;
    
	public ProductForm(MaduraSessionManager maduraSessionManager) {
		super(new VerticalLayout(),maduraSessionManager);
	}

}