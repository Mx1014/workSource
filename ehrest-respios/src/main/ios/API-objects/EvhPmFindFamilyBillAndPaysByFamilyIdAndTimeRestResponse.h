//
// EvhPmFindFamilyBillAndPaysByFamilyIdAndTimeRestResponse.h
// generated at 2016-03-28 15:56:09 
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
