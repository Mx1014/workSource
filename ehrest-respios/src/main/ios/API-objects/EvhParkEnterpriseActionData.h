//
// EvhParkEnterpriseActionData.h
// generated at 2016-04-18 14:48:51 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhParkEnterpriseActionData
//
@interface EvhParkEnterpriseActionData
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* type;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

