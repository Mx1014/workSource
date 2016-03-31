//
// EvhParkEnterpriseActionData.h
// generated at 2016-03-31 10:18:19 
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

