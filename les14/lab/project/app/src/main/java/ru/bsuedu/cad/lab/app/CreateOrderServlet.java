package ru.bsuedu.cad.lab.app;

import ru.bsuedu.cad.lab.entity.Order;
import ru.bsuedu.cad.lab.entity.Customer;
import ru.bsuedu.cad.lab.service.OrderService;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.Collections;
import ru.bsuedu.cad.lab.repository.CustomerRepository;
import java.util.List;
import ru.bsuedu.cad.lab.repository.ProductRepository;
import ru.bsuedu.cad.lab.entity.Product;
import ru.bsuedu.cad.lab.entity.OrderDetail;

@WebServlet("/create-order")
public class CreateOrderServlet extends HttpServlet {
    private OrderService orderService;

    @Override
    public void init() throws ServletException {
        WebApplicationContext context = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
        this.orderService = context.getBean(OrderService.class);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html;charset=UTF-8");
        PrintWriter out = resp.getWriter();
        String contextPath = req.getContextPath();
        // Получаем список клиентов
        WebApplicationContext context = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
        CustomerRepository customerRepository = context.getBean(CustomerRepository.class);
        List<Customer> customers = customerRepository.findAll();
        ProductRepository productRepository = context.getBean(ProductRepository.class);
        List<Product> products = productRepository.findAll();
        out.println("<html><head><title>Создать заказ</title>"
                + "<style>"
                + "body { font-family: 'Segoe UI', Arial, sans-serif; background: #f4f6fb; margin: 0; padding: 0; }"
                + ".container { max-width: 600px; margin: 40px auto; background: #fff; border-radius: 12px; box-shadow: 0 2px 12px #0001; padding: 32px; }"
                + "h1 { text-align: center; color: #2d3a4b; }"
                + "form { display: flex; flex-direction: column; gap: 18px; margin-top: 24px; }"
                + "label { font-weight: 500; color: #2d3a4b; margin-bottom: 4px; display: block; }"
                + "input[type='text'], input[type='email'], input[type='number'] { padding: 10px; border: 1px solid #e0e6ed; border-radius: 6px; font-size: 1rem; display: block; width: 100%; box-sizing: border-box; margin-bottom: 8px; }"
                + ".btn { margin-top: 18px; padding: 12px 28px; background: #4f8cff; color: #fff; border: none; border-radius: 6px; font-size: 1rem; cursor: pointer; text-decoration: none; transition: background 0.2s; }"
                + ".btn:hover { background: #2563eb; }"
                + "a { color: #4f8cff; text-decoration: none; margin-top: 18px; display: inline-block; }"
                + "a:hover { text-decoration: underline; }"
                + "select { padding: 10px; border: 1px solid #e0e6ed; border-radius: 6px; font-size: 1rem; background: #f8fafc; }"
                + "option { background: #fff; color: #2d3a4b; }"
                + ".radio-group { display: flex; gap: 24px; margin-bottom: 10px; }"
                + ".radio-label { display: flex; align-items: center; gap: 8px; font-weight: 500; }"
                + "</style>"
                + "<script>\n"
                + "function toggleClientFields() {\n"
                + "  var isNew = document.getElementById('clientTypeNew').checked;\n"
                + "  document.getElementById('newClientFields').style.display = isNew ? 'block' : 'none';\n"
                + "  document.getElementById('existingClientFields').style.display = isNew ? 'none' : 'block';\n"
                + "  // Управляем required\n"
                + "  document.getElementById('customerName').required = isNew;\n"
                + "  document.getElementById('customerEmail').required = isNew;\n"
                + "}\n"
                + "window.onload = toggleClientFields;\n"
                + "</script>"
                + "</head><body><div class='container'>");
        out.println("<h1>Создать заказ</h1>");
        out.println("<form method='post' action='create-order'>");
        // Radio для выбора типа клиента
        out.println("<div class='radio-group'>"
                + "<label class='radio-label'><input type='radio' id='clientTypeNew' name='clientType' value='new' checked onchange='toggleClientFields()'>Новый клиент</label>"
                + "<label class='radio-label'><input type='radio' id='clientTypeExisting' name='clientType' value='existing' onchange='toggleClientFields()'>Существующий клиент</label>"
                + "</div>");
        // Поля для нового клиента
        out.println("<div id='newClientFields'>");
        out.println("<div><label for='customerName'>Имя клиента:</label><input type='text' id='customerName' name='customerName'></div>");
        out.println("<div><label for='customerEmail'>Email клиента:</label><input type='email' id='customerEmail' name='customerEmail'></div>");
        out.println("<div><label for='customerPhone'>Телефон клиента:</label><input type='text' id='customerPhone' name='customerPhone'></div>");
        out.println("<div><label for='customerAddress'>Адрес клиента:</label><input type='text' id='customerAddress' name='customerAddress'></div>");
        out.println("</div>");
        // Список существующих клиентов
        out.println("<div id='existingClientFields' style='display:none;'>");
        out.println("<label for='existingCustomerId'>Выберите клиента:</label>");
        out.println("<select id='existingCustomerId' name='existingCustomerId'>");
        for (Customer c : customers) {
            out.println("<option value='" + c.getCustomerId() + "'>" + c.getName() + " (" + c.getEmail() + ")</option>");
        }
        out.println("</select></div>");
        // Выбор продуктов
        out.println("<label for='productIds'>Выберите продукты (можно несколько):</label>");
        out.println("<select id='productIds' name='productIds' multiple size='6' style='height:auto;'>");
        for (Product p : products) {
            out.println("<option value='" + p.getProductId() + "'>" + p.getName() + " (" + p.getPrice() + " ₽, " + (p.getCategory() != null ? p.getCategory().getName() : "-") + ")</option>");
        }
        out.println("</select>");
        // Остальные поля заказа
        out.println("<label for='status'>Статус заказа:</label>"
            + "<select id='status' name='status'>"
            + "<option value='NEW' selected>Новый</option>"
            + "<option value='PROCESSING'>В обработке</option>"
            + "<option value='SHIPPED'>Отправлен</option>"
            + "<option value='COMPLETED'>Завершён</option>"
            + "<option value='CANCELLED'>Отменён</option>"
            + "</select>");
        out.println("<label for='totalPrice'>Сумма заказа:</label><input type='number' step='0.01' id='totalPrice' name='totalPrice' value='0.0'>");
        out.println("<label for='shippingAddress'>Адрес доставки:</label><input type='text' id='shippingAddress' name='shippingAddress'>");
        out.println("<button type='submit' class='btn'>Создать</button>");
        out.println("</form>");
        out.println("<a href='" + contextPath + "/orders'>Назад к списку заказов</a>");
        out.println("</div></body></html>");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String clientType = req.getParameter("clientType");
        String status = req.getParameter("status");
        String shippingAddress = req.getParameter("shippingAddress");
        WebApplicationContext context = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
        CustomerRepository customerRepository = context.getBean(CustomerRepository.class);
        ProductRepository productRepository = context.getBean(ProductRepository.class);
        Customer customer = null;
        if ("existing".equals(clientType)) {
            String existingCustomerId = req.getParameter("existingCustomerId");
            if (existingCustomerId != null && !existingCustomerId.isBlank()) {
                try {
                    int id = Integer.parseInt(existingCustomerId);
                    customer = customerRepository.findById(id).orElse(null);
                } catch (NumberFormatException ignored) {}
            }
        } else {
            String customerName = req.getParameter("customerName");
            String customerEmail = req.getParameter("customerEmail");
            String customerPhone = req.getParameter("customerPhone");
            String customerAddress = req.getParameter("customerAddress");
            if (customerName != null && !customerName.isBlank()) {
                customer = new Customer();
                customer.setName(customerName);
                customer.setEmail((customerEmail != null && !customerEmail.isBlank()) ? customerEmail : (System.currentTimeMillis() + "@example.com"));
                customer.setPhone((customerPhone != null && !customerPhone.isBlank()) ? customerPhone : "");
                customer.setAddress((customerAddress != null && !customerAddress.isBlank()) ? customerAddress : "");
                customer = customerRepository.save(customer);
            }
        }
        // Обработка выбранных продуктов
        String[] productIds = req.getParameterValues("productIds");
        java.util.List<OrderDetail> details = new java.util.ArrayList<>();
        double totalPrice = 0.0;
        if (productIds != null) {
            for (String pid : productIds) {
                try {
                    int id = Integer.parseInt(pid);
                    Product product = productRepository.findById(id).orElse(null);
                    if (product != null) {
                        OrderDetail detail = new OrderDetail();
                        detail.setProduct(product);
                        detail.setQuantity(1); // по умолчанию 1, можно доработать для ввода количества
                        detail.setPrice(product.getPrice());
                        details.add(detail);
                        totalPrice += product.getPrice();
                    }
                } catch (NumberFormatException ignored) {}
            }
        }
        if (customer != null && !details.isEmpty()) {
            Order order = new Order();
            order.setOrderDate(java.time.LocalDateTime.now());
            order.setCustomer(customer);
            order.setStatus((status != null && !status.isBlank()) ? status : "NEW");
            order.setTotalPrice(totalPrice);
            order.setShippingAddress((shippingAddress != null && !shippingAddress.isBlank()) ? shippingAddress : "");
            orderService.createOrder(order, details);
        }
        resp.sendRedirect(req.getContextPath() + "/orders");
    }
} 