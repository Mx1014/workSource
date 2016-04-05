//
// EvhTechparkEntryFindLeasePromotionByIdRestResponse.h
// generated at 2016-04-05 13:45:27 
//
#import "RestResponseBase.h"
#import "EvhBuildingForRentDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhTechparkEntryFindLeasePromotionByIdRestResponse
//
@interface EvhTechparkEntryFindLeasePromotionByIdRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhBuildingForRentDTO* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
