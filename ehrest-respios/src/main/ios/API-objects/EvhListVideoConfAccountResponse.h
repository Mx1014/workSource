//
// EvhListVideoConfAccountResponse.h
<<<<<<< HEAD
// generated at 2016-04-18 14:48:50 
=======
// generated at 2016-04-19 14:25:55 
>>>>>>> 3.3.x
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhVideoConfAccountDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListVideoConfAccountResponse
//
@interface EvhListVideoConfAccountResponse
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* nextPageAnchor;

// item type EvhVideoConfAccountDTO*
@property(nonatomic, strong) NSMutableArray* videoConfAccounts;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

