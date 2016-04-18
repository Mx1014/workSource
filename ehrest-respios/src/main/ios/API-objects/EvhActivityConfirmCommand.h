//
// EvhActivityConfirmCommand.h
// generated at 2016-04-18 14:48:52 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhActivityConfirmCommand
//
@interface EvhActivityConfirmCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* rosterId;

@property(nonatomic, copy) NSNumber* confirmFamilyId;

@property(nonatomic, copy) NSString* targetName;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

