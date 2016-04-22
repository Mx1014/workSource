//
// EvhActivityConfirmCommand.h
// generated at 2016-04-22 13:56:46 
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

