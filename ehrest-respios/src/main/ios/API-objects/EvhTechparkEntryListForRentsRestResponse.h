//
// EvhTechparkEntryListForRentsRestResponse.h
// generated at 2016-03-31 11:07:27 
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
