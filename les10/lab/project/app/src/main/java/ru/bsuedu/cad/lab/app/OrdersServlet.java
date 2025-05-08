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
import java.util.List;

@WebServlet("/orders")
public class OrdersServlet extends HttpServlet {
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
        List<Order> orders = orderService.getAllOrders();
        out.println("<html><head><title>Список заказов</title>"
                + "<style>"
                + "body { font-family: 'Segoe UI', Arial, sans-serif; background: #f4f6fb; margin: 0; padding: 0; }"
                + ".container { max-width: 1100px; margin: 40px auto; background: #fff; border-radius: 12px; box-shadow: 0 2px 12px #0001; padding: 32px; }"
                + "h1 { text-align: center; color: #2d3a4b; }"
                + "table { width: 100%; border-collapse: collapse; margin-top: 24px; }"
                + "th, td { padding: 12px 8px; border-bottom: 1px solid #e0e6ed; text-align: left; }"
                + "th { background: #eaf1fb; color: #2d3a4b; font-weight: 600; }"
                + "tr:hover { background: #f5faff; }"
                + ".btn { display: inline-block; margin: 24px 0 0 0; padding: 12px 28px; background: #4f8cff; color: #fff; border: none; border-radius: 6px; font-size: 1rem; cursor: pointer; text-decoration: none; transition: background 0.2s; }"
                + ".btn:hover { background: #2563eb; }"
                + "</style></head><body><div class='container'>");
        out.println("<h1>Список заказов</h1>");
        out.println("<table><tr>"
                + "<th>ID</th>"
                + "<th>Дата</th>"
                + "<th>Статус</th>"
                + "<th>Сумма</th>"
                + "<th>Адрес доставки</th>"
                + "<th>Клиент (ID)</th>"
                + "<th>Имя клиента</th>"
                + "<th>Email</th>"
                + "<th>Телефон</th>"
                + "<th>Адрес клиента</th>"
                + "</tr>");
        for (Order order : orders) {
            Customer c = order.getCustomer();
            out.println("<tr>"
                    + "<td>" + order.getOrderId() + "</td>"
                    + "<td>" + (order.getOrderDate() != null ? order.getOrderDate().toString().replace('T', ' ') : "-") + "</td>"
                    + "<td>" + (order.getStatus() != null ? order.getStatus() : "-") + "</td>"
                    + "<td>" + (order.getTotalPrice() != null ? String.format("%.2f", order.getTotalPrice()) : "-") + "</td>"
                    + "<td>" + (order.getShippingAddress() != null ? order.getShippingAddress() : "-") + "</td>"
                    + "<td>" + (c != null && c.getCustomerId() != null ? c.getCustomerId() : "-") + "</td>"
                    + "<td>" + (c != null && c.getName() != null ? c.getName() : "-") + "</td>"
                    + "<td>" + (c != null && c.getEmail() != null ? c.getEmail() : "-") + "</td>"
                    + "<td>" + (c != null && c.getPhone() != null ? c.getPhone() : "-") + "</td>"
                    + "<td>" + (c != null && c.getAddress() != null ? c.getAddress() : "-") + "</td>"
                    + "</tr>");
        }
        out.println("</table>");
        String contextPath = req.getContextPath();
        out.println("<form action='" + contextPath + "/create-order' method='get'>"
                + "<button type='submit' class='btn'>Создать заказ</button></form>");
        out.println("</div></body></html>");
    }
} 