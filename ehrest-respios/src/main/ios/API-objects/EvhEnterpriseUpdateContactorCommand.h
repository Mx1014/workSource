//
// EvhEnterpriseUpdateContactorCommand.h
<<<<<<< HEAD
<<<<<<< HEAD
// generated at 2016-04-18 14:48:52 
=======
// generated at 2016-04-12 19:00:53 
>>>>>>> 3.3.x
=======
// generated at 2016-04-22 13:56:46 
>>>>>>> 3.3.x
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhEnterpriseUpdateContactorCommand
//
@interface EvhEnterpriseUpdateContactorCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* contactName;

@property(nonatomic, copy) NSString* entryValue;

@property(nonatomic, copy) NSNumber* enterpriseId;

@property(nonatomic, copy) NSNumber* communityId;

@property(nonatomic, copy) NSNumber* namespaceId;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

