//
// EvhTechparkEntryFindLeasePromotionByIdRestResponse.h
// generated at 2016-04-12 15:02:21 
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
