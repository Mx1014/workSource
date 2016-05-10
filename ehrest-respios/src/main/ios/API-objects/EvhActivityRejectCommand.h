//
// EvhActivityRejectCommand.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhActivityRejectCommand
//
@interface EvhActivityRejectCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* rosterId;

@property(nonatomic, copy) NSNumber* familyId;

@property(nonatomic, copy) NSString* reason;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

