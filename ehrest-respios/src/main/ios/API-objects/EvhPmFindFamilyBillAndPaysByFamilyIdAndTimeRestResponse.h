//
// EvhPmFindFamilyBillAndPaysByFamilyIdAndTimeRestResponse.h
// generated at 2016-04-08 20:09:24 
//
#import "RestResponseBase.h"
#import "EvhPmBillsDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhPmFindFamilyBillAndPaysByFamilyIdAndTimeRestResponse
//
@interface EvhPmFindFamilyBillAndPaysByFamilyIdAndTimeRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhPmBillsDTO* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
