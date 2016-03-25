//
// EvhTechparkEntryListForRentsRestResponse.h
// generated at 2016-03-25 09:26:44 
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
