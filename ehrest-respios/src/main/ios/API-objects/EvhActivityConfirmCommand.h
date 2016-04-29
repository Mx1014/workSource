//
// EvhActivityConfirmCommand.h
<<<<<<< HEAD
<<<<<<< HEAD
// generated at 2016-04-18 14:48:52 
=======
// generated at 2016-04-19 14:25:56 
>>>>>>> 3.3.x
=======
// generated at 2016-04-26 18:22:56 
>>>>>>> 3.3.x
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

