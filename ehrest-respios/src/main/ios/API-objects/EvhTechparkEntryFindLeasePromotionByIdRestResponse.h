//
// EvhTechparkEntryFindLeasePromotionByIdRestResponse.h
// generated at 2016-03-25 19:05:21 
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
