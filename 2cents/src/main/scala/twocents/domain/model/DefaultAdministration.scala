package twocents.domain.model

import org.bigsaas.core.model.Id

object DefaultAdministration {
  implicit def administration = Administration(Ledger(accounts.flatten), Seq.empty, documentKinds)
  
  implicit class TreeBuilder[Node](node : Node) { self =>
    val child : Seq[TreeBuilder[Node]] = Seq.empty
    def ++(builder : TreeBuilder[Node]) = 
      /++ (_ => builder) 
    def /++(f : Node => TreeBuilder[Node]) = 
      new TreeBuilder(node) { 
        override val child = self.child ++ Seq(f(node))
      }
    def flatten : Seq[Node] = 
      Seq(node) ++ child.flatMap(_.flatten)
    override def toString = flatten.mkString("\n")
  }
  
  def accounts = 
    Account(Id.generate, "BAL", "Balans", None) /++ { parent =>
      Account(Id.generate, "ACT", "Activa", Some(parent)) /++ { parent =>
        Account(Id.generate, "00", "Vaste activa", Some(parent)) /++ { parent =>
          Account(Id.generate, "010", "Materiele vaste activa", Some(parent)) /++ { parent =>
            Account(Id.generate, "0111", "ICT apperatuur", Some(parent))
            Account(Id.generate, "0112", "Afschrijving ICT apperatuur", Some(parent))
          }
        }
      }
    }
    Account(Id.generate, "111", "ICT apperatuur", None) ++
    Account(Id.generate, "113", "Computer software", None) ++
    Account(Id.generate, "8", "Opbrengsten", None) /++ { parent => 
      Account(Id.generate, "80", "Omzet", Some(parent)) /++ { parent =>  
        Account(Id.generate, "8000", "Management fee", Some(parent)) 
      } ++
      Account(Id.generate, "82", "Rente / Financieringsopbrengsten", Some(parent)) /++ { parent =>  
        Account(Id.generate, "8200", "Ontvangen rente", Some(parent)) ++
        Account(Id.generate, "8201", "Ontvangen dividend", Some(parent)) 
      }
    }
  
  val highVat = Vat(Id.generate, "hoog", 21)
  def documentKinds = Seq(
      DocumentKind(Id.generate, "Inkoopfactuur", Seq.empty, highVat.id),
      DocumentKind(Id.generate, "Verkoopfactuur", Seq.empty, highVat.id))
}

object ModelTest extends App {
  
  println(DefaultAdministration.administration)
}

// balans
//   activa
//     materiele vast activa
//       kantoorinventaris
//     debiteuren
//     kas
//   passiva
//     crediteuren
// Winst & Verlies
//   kosten
//     kasverschillen
//   opbrengsten

