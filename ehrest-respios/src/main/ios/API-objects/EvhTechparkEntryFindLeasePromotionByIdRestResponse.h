//
// EvhTechparkEntryFindLeasePromotionByIdRestResponse.h
// generated at 2016-04-06 19:59:47 
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
