//
// EvhTechparkEntryListForRentsRestResponse.h
// generated at 2016-04-01 15:40:24 
//
#import "RestResponseBase.h"
#import "EvhListBuildingForRentResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhTechparkEntryListForRentsRestResponse
//
@interface EvhTechparkEntryListForRentsRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhListBuildingForRentResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
