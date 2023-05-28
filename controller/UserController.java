package team3.meowie.member.controller;

import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import team3.meowie.member.model.User;
import team3.meowie.member.model.UserProfile;
import team3.meowie.member.service.IUserService;
import team3.meowie.ticket.model.Ticket;
import team3.meowie.ticket.service.TicketService;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Controller
public class UserController {

    @Autowired
    private IUserService userService;

    @GetMapping("/login")
    public String showLoginPage() {
        if (userService.isLogin())
            return "redirect:/";

        return "member/login";
    }

    @GetMapping("/user/center")
    public String showMember(Model model) {
        String username = userService.getLoginUsername();
        UserProfile profile = userService.getUserProfileByUsername(username);
        model.addAttribute("profile", profile);
        return "member/center";
    }

    @GetMapping("/user/resetPassword")
    public String showForgotPasswrdPage() {
        if (userService.isLogin())
            return "redirect:/";

        return "member/forgotPassword";
    }

    @GetMapping("/user/center/changePassword")
    public String showChangePasswordPage(Model model) {
        if (!userService.isLogin())
            return "redirect:/login";
        String username = userService.getLoginUsername();
        model.addAttribute("username", username);
        return "member/changePassword";
    }
@Autowired
TicketService ticketService;
    @GetMapping("/user/center/Tickets")
    public String showTicketsPage(Model model) {
        if (!userService.isLogin())
            return "redirect:/login";
        User user= userService.findUserByUsername(userService.getLoginUsername());
        model.addAttribute("tickets", ticketService.getTicketsByUser(user));
        return "member/Tickets";
    }



    @GetMapping("/user/center/Tickets/qr/{ticketId}")
    public String showTicketQR(Model model, @PathVariable("ticketId") Long ticketId) throws WriterException, IOException {
        if (!userService.isLogin()) {
            return "redirect:/login";
        }

        String content = ticketId.toString(); // QR code字串
        int width = 200;
        int height = 200;

        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(content, com.google.zxing.BarcodeFormat.QR_CODE, width, height);
        User user= userService.findUserByUsername(userService.getLoginUsername());
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        String base64Image = Base64.encodeBase64String(byteArray);
        Ticket ticket = ticketService.findById(ticketId);
        model.addAttribute("ticket", ticket);
        model.addAttribute("base64Image", base64Image);
        model.addAttribute("user", user);

        return "member/Ticketsqr";
    }



}
